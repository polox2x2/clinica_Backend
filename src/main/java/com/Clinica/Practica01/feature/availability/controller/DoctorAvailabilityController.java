package com.Clinica.Practica01.feature.availability.controller;

import com.Clinica.Practica01.core.security.PermissionChecker;
import com.Clinica.Practica01.core.web.BaseCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.Clinica.Practica01.feature.availability.dto.DoctorAvailabilityRequest;
import com.Clinica.Practica01.feature.availability.dto.DoctorAvailabilityResponse;
import com.Clinica.Practica01.feature.availability.service.DoctorAvailabilityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor-availabilities")
@Tag(name = "Availability", description = "Plantilla semanal de disponibilidad del medico")
public class DoctorAvailabilityController
        extends BaseCrudController<DoctorAvailabilityRequest, DoctorAvailabilityResponse> {

    public DoctorAvailabilityController(DoctorAvailabilityService service, PermissionChecker permissions) {
        super(service, permissions);
    }

    @Override
    protected String permissionPrefix() {
        return "Availability";
    }
}
