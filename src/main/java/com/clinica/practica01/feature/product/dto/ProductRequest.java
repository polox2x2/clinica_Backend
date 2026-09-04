package com.clinica.practica01.feature.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    // Stock inicial (luego se ajusta con entradas/ventas)
    @NotNull
    @PositiveOrZero
    private Integer stock;
}
