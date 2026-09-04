package com.Clinica.Practica01.feature.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequest {
    // Cliente opcional (paciente registrado)
    private UUID patientId;
    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}
