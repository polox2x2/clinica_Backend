package com.clinica.practica01.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * Envelope estandar para respuestas de lista paginadas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    private List<T> items;
    private int page;
    private int pageSize;
    private long totalCount;
    private int totalPages;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private boolean isEmpty;

    /**
     * Construye el envelope a partir de un Page de Spring (0-based) mapeando el
     * contenido al DTO de respuesta. Expone page en base 1.
     */
    public static <E, R> PagedResponse<R> from(Page<E> page, Function<E, R> mapper) {
        List<R> items = page.getContent().stream().map(mapper).toList();
        return new PagedResponse<>(
                items,
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                page.isEmpty()
        );
    }
}
