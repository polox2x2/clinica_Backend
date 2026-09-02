package com.Clinica.Practica01.feature.paciente.controller;

import com.Clinica.Practica01.feature.paciente.dto.PatientRequestDto;
import com.Clinica.Practica01.feature.paciente.dto.PatientResponseDto;
import com.Clinica.Practica01.feature.paciente.service.PatientService;
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
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "Endpoints para la gestión de Patient")
public class PatientController {

    private final PatientService service;

    // Obtiene una lista con todos los registros de Patient disponibles en el sistema
    @Operation(summary = "Obtener todos los Patient", description = "Recupera una lista completa de todos los Patient registrados en la base de datos sin paginación.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<List<PatientResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Obtiene los detalles de un Patient específico mediante su identificador (ID)
    @Operation(summary = "Obtener Patient por ID", description = "Busca y retorna los detalles de un Patient específico utilizando su ID único. Retorna 404 si no se encuentra.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    public ResponseEntity<PatientResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Crea un nuevo registro de Patient con los datos proporcionados
    @Operation(summary = "Crear nuevo Patient", description = "Registra un nuevo Patient en el sistema validando los campos obligatorios del DTO de entrada.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientResponseDto> create(@Valid @RequestBody PatientRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    // Actualiza la información de un Patient existente utilizando su ID
    @Operation(summary = "Actualizar Patient", description = "Modifica los datos de un Patient existente en base a su ID. Valida que los nuevos datos sean correctos.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado para actualizar"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientResponseDto> update(@PathVariable Long id, @Valid @RequestBody PatientRequestDto request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // Elimina de forma permanente un Patient del sistema
    @Operation(summary = "Eliminar Patient", description = "Elimina físicamente un Patient de la base de datos utilizando su ID. No retorna contenido si es exitoso.")
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
