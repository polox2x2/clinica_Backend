package com.Clinica.Practica01.core.mapper;

import com.Clinica.Practica01.core.domain.BaseEntity;
import com.Clinica.Practica01.core.dto.BaseResponse;

/**
 * Contrato de mapeo entre la entidad y sus DTOs de request/response.
 * Cada feature implementa su propio mapper.
 *
 * @param <E>   entidad
 * @param <Req> DTO de entrada (create/update)
 * @param <Res> DTO de salida
 */
public interface BaseMapper<E extends BaseEntity, Req, Res extends BaseResponse> {

    /** Crea una entidad nueva a partir del request. */
    E toEntity(Req request);

    /** Aplica los cambios del request sobre una entidad existente. */
    void updateEntity(E entity, Req request);

    /** Convierte la entidad a su DTO de respuesta (sin los campos base). */
    Res toResponse(E entity);

    /** Rellena los campos comunes (id/active/createdAt) en la respuesta. */
    default Res toResponseWithBase(E entity) {
        Res res = toResponse(entity);
        res.setId(entity.getId());
        res.setActive(entity.isActive());
        res.setCreatedAt(entity.getCreatedAt());
        return res;
    }
}
