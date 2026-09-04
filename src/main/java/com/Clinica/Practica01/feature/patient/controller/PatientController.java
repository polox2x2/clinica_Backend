package com.Clinica.Practica01.feature.patient.controller;

import com.Clinica.Practica01.core.security.PermissionChecker;
import com.Clinica.Practica01.core.web.BaseCrudController;
import com.Clinica.Practica01.feature.patient.dto.PatientRequest;
import com.Clinica.Practica01.feature.patient.dto.PatientResponse;
import com.Clinica.Practica01.feature.patient.dto.SelfPatientRequest;
import com.Clinica.Practica01.feature.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patient", description = "Pacientes (alta por admin crea su cuenta; el paciente completa su propio perfil en /me)")
public class PatientController extends BaseCrudController<PatientRequest, PatientResponse> {

    private final PatientService patientService;

    public PatientController(PatientService service, PermissionChecker permissions) {
        super(service, permissions);
        this.patientService = service;
    }

    @Override
    protected String permissionPrefix() {
        return "Patient";
    }

    // ---- Self-service (el propio paciente) --------------------------------

    @Operation(summary = "Mi perfil de paciente",
            description = "Perfil del usuario autenticado. 404 si aun no lo creo.")
    @GetMapping("/me")
    public ResponseEntity<PatientResponse> getMe(Authentication auth) {
        return ResponseEntity.ok(patientService.getMe(auth.getName()));
    }

    @Operation(summary = "Crear mi perfil de paciente",
            description = "El paciente (auto-registrado) completa su perfil para poder reservar citas.")
    @PostMapping("/me")
    public ResponseEntity<PatientResponse> createMe(@Valid @RequestBody SelfPatientRequest request,
                                                    Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createMe(auth.getName(), request));
    }

    @Operation(summary = "Actualizar mi perfil de paciente")
    @PutMapping("/me")
    public ResponseEntity<PatientResponse> updateMe(@Valid @RequestBody SelfPatientRequest request,
                                                    Authentication auth) {
        return ResponseEntity.ok(patientService.updateMe(auth.getName(), request));
    }
}
