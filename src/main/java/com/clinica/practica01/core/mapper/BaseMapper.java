package com.clinica.practica01.core.mapper;

import com.clinica.practica01.core.domain.BaseEntity;
import com.clinica.practica01.core.dto.BaseResponse;

/**
 * Contrato de mapeo entre la entidad y sus DTOs de request/response.
 * Cada feature implementa su propio mapper.
 *
 * @param <E> entidad
 * @param <Q> DTO de entrada (create/update)
 * @param <R> DTO de salida
 */
public interface BaseMapper<E extends BaseEntity, Q, R extends BaseResponse> {

    /** Crea una entidad nueva a partir del request. */
    E toEntity(Q request);

    /** Aplica los cambios del request sobre una entidad existente. */
    void updateEntity(E entity, Q request);

    /** Convierte la entidad a su DTO de respuesta (sin los campos base). */
    R toResponse(E entity);

    /** Rellena los campos comunes (id/active/createdAt) en la respuesta. */
    default R toResponseWithBase(E entity) {
        R res = toResponse(entity);
        res.setId(entity.getId());
        res.setActive(entity.isActive());
        res.setCreatedAt(entity.getCreatedAt());
        return res;
    }
}
