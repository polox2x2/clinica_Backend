package com.Clinica.Practica01.core.dto;

import lombok.Data;

/**
 * Parametros estandar de busqueda/paginacion para los GET de lista.
 * Se enlaza desde el query string (?search=&sortBy=&sortDirection=&page=&pageSize=).
 */
@Data
public class SearchParams {
    private String search;
    private String sortBy;
    private String sortDirection = "Ascending"; // Ascending | Descending
    private int page = 1;
    private int pageSize = 50;

    public boolean isDescending() {
        return sortDirection != null && sortDirection.equalsIgnoreCase("Descending");
    }

    public int safePage() {
        return Math.max(page, 1);
    }

    public int safePageSize() {
        return (pageSize < 1 || pageSize > 200) ? 50 : pageSize;
    }
}
