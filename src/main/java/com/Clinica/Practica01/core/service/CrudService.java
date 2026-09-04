package com.Clinica.Practica01.core.service;

import com.Clinica.Practica01.core.dto.PagedResponse;
import com.Clinica.Practica01.core.dto.SearchParams;

import java.util.UUID;

/**
 * Contrato CRUD generico. Cada feature declara su propia interface de servicio
 * extendiendo esta (ej. UserService extends CrudService&lt;UserRequest, UserResponse&gt;),
 * y su *ServiceImpl la implementa via AbstractCrudService.
 */
public interface CrudService<Req, Res> {

    Res create(Req request);

    Res findById(UUID id);

    Res update(UUID id, Req request);

    /** Borrado logico. */
    void delete(UUID id);

    PagedResponse<Res> search(SearchParams params);
}
