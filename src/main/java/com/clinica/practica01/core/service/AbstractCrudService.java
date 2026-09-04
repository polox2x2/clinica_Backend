package com.clinica.practica01.core.service;

import com.clinica.practica01.core.domain.BaseEntity;
import com.clinica.practica01.core.dto.BaseResponse;
import com.clinica.practica01.core.dto.PagedResponse;
import com.clinica.practica01.core.dto.SearchParams;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.core.repository.BaseRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementacion CRUD generica (borrado logico + busqueda + paginacion).
 * Los *ServiceImpl de cada feature la extienden e implementan su interface XService.
 */
public abstract class AbstractCrudService<E extends BaseEntity, Req, Res extends BaseResponse>
        implements CrudService<Req, Res> {

    protected final BaseRepository<E> repository;
    protected final BaseMapper<E, Req, Res> mapper;

    protected AbstractCrudService(BaseRepository<E> repository, BaseMapper<E, Req, Res> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /** Nombre del recurso para mensajes de error (ej. "User"). */
    protected abstract String resourceName();

    /** Campos sobre los que aplica 'search' (vacio = sin busqueda por texto). */
    protected List<String> searchableFields() {
        return List.of();
    }

    @Override
    public Res create(Req request) {
        E entity = mapper.toEntity(request);
        entity.setActive(true);
        return mapper.toResponseWithBase(repository.save(entity));
    }

    @Override
    public Res findById(UUID id) {
        return mapper.toResponseWithBase(getActiveOrThrow(id));
    }

    @Override
    public Res update(UUID id, Req request) {
        E entity = getActiveOrThrow(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponseWithBase(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        E entity = getActiveOrThrow(id);
        entity.setActive(false);
        repository.save(entity);
    }

    @Override
    public PagedResponse<Res> search(SearchParams params) {
        Sort sort = buildSort(params);
        Pageable pageable = PageRequest.of(params.safePage() - 1, params.safePageSize(), sort);
        Page<E> page = repository.findAll(buildSpecification(params.getSearch()), pageable);
        return PagedResponse.from(page, mapper::toResponseWithBase);
    }

    /** Entidad activa por id o 404. Reutilizable por los servicios de feature. */
    protected E getActiveOrThrow(UUID id) {
        return repository.findById(id)
                .filter(BaseEntity::isActive)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName() + " no encontrado: " + id));
    }

    private Sort buildSort(SearchParams params) {
        String field = (params.getSortBy() == null || params.getSortBy().isBlank())
                ? "createdAt"
                : params.getSortBy();
        return params.isDescending() ? Sort.by(field).descending() : Sort.by(field).ascending();
    }

    private Specification<E> buildSpecification(String search) {
        return (root, query, cb) -> {
            List<Predicate> ands = new ArrayList<>();
            ands.add(cb.isTrue(root.get("active")));
            if (search != null && !search.isBlank() && !searchableFields().isEmpty()) {
                String like = "%" + search.toLowerCase() + "%";
                List<Predicate> ors = new ArrayList<>();
                for (String f : searchableFields()) {
                    // Soporta rutas anidadas via join, ej. "user.firstName"
                    jakarta.persistence.criteria.Path<?> path = root;
                    for (String part : f.split("\\.")) {
                        path = path.get(part);
                    }
                    ors.add(cb.like(cb.lower(path.as(String.class)), like));
                }
                ands.add(cb.or(ors.toArray(new Predicate[0])));
            }
            return cb.and(ands.toArray(new Predicate[0]));
        };
    }
}
