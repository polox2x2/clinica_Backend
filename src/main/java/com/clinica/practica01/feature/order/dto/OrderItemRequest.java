package com.clinica.practica01.feature.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemRequest {
    @NotNull
    private UUID productId;
    @NotNull
    @Positive
    private Integer quantity;
}
