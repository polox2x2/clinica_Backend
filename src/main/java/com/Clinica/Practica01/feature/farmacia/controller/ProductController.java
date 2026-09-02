package com.Clinica.Practica01.feature.farmacia.controller;

import com.Clinica.Practica01.feature.farmacia.dto.ProductRequestDto;
import com.Clinica.Practica01.feature.farmacia.dto.ProductResponseDto;
import com.Clinica.Practica01.feature.farmacia.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Endpoints para la gestión de Product")
public class ProductController {

    private final ProductService service;

    // Obtiene una lista con todos los registros de Product disponibles en el sistema
    @Operation(summary = "Obtener todos los Product", description = "Recupera una lista completa de todos los Product registrados en la base de datos sin paginación.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Obtiene los detalles de un Product específico mediante su identificador (ID)
    @Operation(summary = "Obtener Product por ID", description = "Busca y retorna los detalles de un Product específico utilizando su ID único. Retorna 404 si no se encuentra.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Crea un nuevo registro de Product con los datos proporcionados
    @Operation(summary = "Crear nuevo Product", description = "Registra un nuevo Product en el sistema validando los campos obligatorios del DTO de entrada.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    // Actualiza la información de un Product existente utilizando su ID
    @Operation(summary = "Actualizar Product", description = "Modifica los datos de un Product existente en base a su ID. Valida que los nuevos datos sean correctos.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado para actualizar"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDto request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // Elimina de forma permanente un Product del sistema
    @Operation(summary = "Eliminar Product", description = "Elimina físicamente un Product de la base de datos utilizando su ID. No retorna contenido si es exitoso.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado para eliminar")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
