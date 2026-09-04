package com.Clinica.Practica01.feature.absence.controller;

import com.Clinica.Practica01.core.security.PermissionChecker;
import com.Clinica.Practica01.core.web.BaseCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.Clinica.Practica01.feature.absence.dto.DoctorAbsenceRequest;
import com.Clinica.Practica01.feature.absence.dto.DoctorAbsenceResponse;
import com.Clinica.Practica01.feature.absence.service.DoctorAbsenceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor-absences")
@Tag(name = "Absence", description = "Ausencias/bloqueos del medico")
public class DoctorAbsenceController
        extends BaseCrudController<DoctorAbsenceRequest, DoctorAbsenceResponse> {

    public DoctorAbsenceController(DoctorAbsenceService service, PermissionChecker permissions) {
        super(service, permissions);
    }

    @Override
    protected String permissionPrefix() {
        return "Absence";
    }
}
