package com.Clinica.Practica01.feature.farmacia.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
}
