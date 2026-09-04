package com.clinica.practica01.core.service;

import com.clinica.practica01.core.dto.PagedResponse;
import com.clinica.practica01.core.dto.SearchParams;

import java.util.UUID;

/**
 * Contrato CRUD generico. Cada feature declara su propia interface de servicio
 * extendiendo esta (ej. UserService extends CrudService&lt;UserRequest, UserResponse&gt;),
 * y su *ServiceImpl la implementa via AbstractCrudService.
 */
public interface CrudService<Q, R> {

    R create(Q request);

    R findById(UUID id);

    R update(UUID id, Q request);

    /** Borrado logico. */
    void delete(UUID id);

    PagedResponse<R> search(SearchParams params);
}
