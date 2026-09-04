package com.clinica.practica01.feature.medicalrecord.controller;

import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.feature.medicalrecord.dto.MedicalRecordEntryRequest;
import com.clinica.practica01.feature.medicalrecord.dto.MedicalRecordEntryResponse;
import com.clinica.practica01.feature.medicalrecord.dto.MedicalRecordResponse;
import com.clinica.practica01.feature.medicalrecord.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@Tag(name = "MedicalRecord", description = "Historia clinica: una por paciente, con atenciones que registran los medicos")
public class MedicalRecordController {

    private static final String PREFIX = "MedicalRecord";

    private final MedicalRecordService service;
    private final PermissionChecker permissions;

    @Operation(summary = "Registrar atencion en la historia",
            description = "El medico (del token) agrega una entrada a la historia del paciente. "
                    + "Requiere MedicalRecord:Create.")
    @PostMapping("/entries")
    public ResponseEntity<MedicalRecordEntryResponse> addEntry(
            @Valid @RequestBody MedicalRecordEntryRequest request, Authentication auth) {
        permissions.require(PREFIX + ":Create");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addEntry(request, auth.getName()));
    }

    @Operation(summary = "Mi historia clinica",
            description = "La historia del paciente autenticado (para el portal del paciente).")
    @GetMapping("/me")
    public ResponseEntity<MedicalRecordResponse> getMine(Authentication auth) {
        return ResponseEntity.ok(service.getMine(auth.getName()));
    }

    @Operation(summary = "Historia clinica de un paciente",
            description = "Devuelve la historia completa (datos + atenciones ordenadas). Requiere MedicalRecord:Read.")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<MedicalRecordResponse> getByPatient(@PathVariable UUID patientId) {
        permissions.require(PREFIX + ":Read");
        return ResponseEntity.ok(service.getByPatient(patientId));
    }
}
