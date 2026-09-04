package com.Clinica.Practica01.feature.stockentry.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class StockEntryRequest {
    @NotNull
    private UUID productId;
    @NotNull
    @Positive
    private Integer quantity;
    private BigDecimal unitCost;
    private String note;
}
