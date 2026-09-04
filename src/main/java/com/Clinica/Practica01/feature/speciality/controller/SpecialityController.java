package com.Clinica.Practica01.feature.speciality.controller;

import com.Clinica.Practica01.core.security.PermissionChecker;
import com.Clinica.Practica01.core.web.BaseCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.Clinica.Practica01.feature.speciality.dto.SpecialityRequest;
import com.Clinica.Practica01.feature.speciality.dto.SpecialityResponse;
import com.Clinica.Practica01.feature.speciality.service.SpecialityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/specialities")
@Tag(name = "Speciality", description = "Especialidades medicas (con subespecialidad)")
public class SpecialityController extends BaseCrudController<SpecialityRequest, SpecialityResponse> {

    public SpecialityController(SpecialityService service, PermissionChecker permissions) {
        super(service, permissions);
    }

    @Override
    protected String permissionPrefix() {
        return "Speciality";
    }
}
