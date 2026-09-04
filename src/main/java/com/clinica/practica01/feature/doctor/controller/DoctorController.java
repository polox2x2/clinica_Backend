package com.clinica.practica01.feature.doctor.controller;

import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.core.web.BaseCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.clinica.practica01.feature.doctor.dto.DoctorRequest;
import com.clinica.practica01.feature.doctor.dto.DoctorResponse;
import com.clinica.practica01.feature.doctor.service.DoctorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/doctors")
@Tag(name = "Doctor", description = "Medicos (crea tambien su cuenta de acceso)")
public class DoctorController extends BaseCrudController<DoctorRequest, DoctorResponse> {

    private final DoctorService doctorService;

    public DoctorController(DoctorService service, PermissionChecker permissions) {
        super(service, permissions);
        this.doctorService = service;
    }

    @GetMapping("/me")
    public ResponseEntity<DoctorResponse> getMe(Authentication authentication) {
        return ResponseEntity.ok(doctorService.getMe(authentication.getName()));
    }

    @Override
    protected String permissionPrefix() {
        return "Doctor";
    }
}
