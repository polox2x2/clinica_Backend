package com.Clinica.Practica01.feature.schedule.controller;

import com.Clinica.Practica01.core.security.PermissionChecker;
import com.Clinica.Practica01.core.web.BaseCrudController;
import com.Clinica.Practica01.feature.schedule.dto.GenerateScheduleRequest;
import com.Clinica.Practica01.feature.schedule.dto.GenerateScheduleResponse;
import com.Clinica.Practica01.feature.schedule.dto.ScheduleRequest;
import com.Clinica.Practica01.feature.schedule.dto.ScheduleResponse;
import com.Clinica.Practica01.feature.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedule", description = "Franjas de atencion (slots). Se pueden generar desde la plantilla del medico")
public class ScheduleController extends BaseCrudController<ScheduleRequest, ScheduleResponse> {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService service, PermissionChecker permissions) {
        super(service, permissions);
        this.scheduleService = service;
    }

    @Override
    protected String permissionPrefix() {
        return "Schedule";
    }

    @Operation(summary = "Generar bloques desde la plantilla",
            description = "Genera los slots del medico en un rango de fechas segun su plantilla semanal "
                    + "(dia/horario/duracion), saltando ausencias y duplicados. Emite evento de calendario. "
                    + "Requiere Schedule:Create.")
    @PostMapping("/generate")
    public ResponseEntity<GenerateScheduleResponse> generate(
            @Valid @RequestBody GenerateScheduleRequest request) {
        permissions.require("Schedule:Create");
        return ResponseEntity.ok(scheduleService.generate(request));
    }
}
