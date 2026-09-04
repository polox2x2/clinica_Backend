package com.clinica.practica01.feature.order.controller;

import com.clinica.practica01.core.dto.PagedResponse;
import com.clinica.practica01.core.dto.SearchParams;
import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.feature.order.dto.OrderRequest;
import com.clinica.practica01.feature.order.dto.OrderResponse;
import com.clinica.practica01.feature.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Ventas de farmacia: valida y descuenta stock, calcula el total")
public class OrderController {

    private static final String PREFIX = "Order";

    private final OrderService service;
    private final PermissionChecker permissions;

    @Operation(summary = "Registrar venta",
            description = "Crea la venta con sus items: valida stock, lo descuenta y calcula el total. "
                    + "Requiere Order:Create.")
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        permissions.require(PREFIX + ":Create");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Listar ventas", description = "Requiere Order:List.")
    @GetMapping
    public ResponseEntity<PagedResponse<OrderResponse>> list(SearchParams params) {
        permissions.require(PREFIX + ":List");
        return ResponseEntity.ok(service.search(params));
    }

    @Operation(summary = "Obtener venta por id", description = "Requiere Order:Read.")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable UUID id) {
        permissions.require(PREFIX + ":Read");
        return ResponseEntity.ok(service.findById(id));
    }
}
