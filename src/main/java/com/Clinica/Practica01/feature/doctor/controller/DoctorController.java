package com.Clinica.Practica01.feature.doctor.controller;

import com.Clinica.Practica01.core.security.PermissionChecker;
import com.Clinica.Practica01.core.web.BaseCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.Clinica.Practica01.feature.doctor.dto.DoctorRequest;
import com.Clinica.Practica01.feature.doctor.dto.DoctorResponse;
import com.Clinica.Practica01.feature.doctor.service.DoctorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctors")
@Tag(name = "Doctor", description = "Medicos (crea tambien su cuenta de acceso)")
public class DoctorController extends BaseCrudController<DoctorRequest, DoctorResponse> {

    public DoctorController(DoctorService service, PermissionChecker permissions) {
        super(service, permissions);
    }

    @Override
    protected String permissionPrefix() {
        return "Doctor";
    }
}
