package com.clinica.practica01.feature.stockentry.controller;

import com.clinica.practica01.core.dto.PagedResponse;
import com.clinica.practica01.core.dto.SearchParams;
import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.feature.stockentry.dto.StockEntryRequest;
import com.clinica.practica01.feature.stockentry.dto.StockEntryResponse;
import com.clinica.practica01.feature.stockentry.service.StockEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stock-entries")
@RequiredArgsConstructor
@Tag(name = "StockEntry", description = "Entradas de inventario: registran ingreso de unidades y suman al stock del producto")
public class StockEntryController {

    private static final String PREFIX = "StockEntry";

    private final StockEntryService service;
    private final PermissionChecker permissions;

    @Operation(summary = "Registrar entrada de stock",
            description = "Suma las unidades al stock del producto. Requiere StockEntry:Create.")
    @PostMapping
    public ResponseEntity<StockEntryResponse> register(@Valid @RequestBody StockEntryRequest request) {
        permissions.require(PREFIX + ":Create");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Listar entradas de stock", description = "Requiere StockEntry:List.")
    @GetMapping
    public ResponseEntity<PagedResponse<StockEntryResponse>> list(SearchParams params) {
        permissions.require(PREFIX + ":List");
        return ResponseEntity.ok(service.search(params));
    }

    @Operation(summary = "Obtener entrada por id", description = "Requiere StockEntry:Read.")
    @GetMapping("/{id}")
    public ResponseEntity<StockEntryResponse> getById(@PathVariable UUID id) {
        permissions.require(PREFIX + ":Read");
        return ResponseEntity.ok(service.findById(id));
    }
}
