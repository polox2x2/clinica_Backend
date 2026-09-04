package com.clinica.practica01.feature.appointment.controller;

import com.clinica.practica01.core.dto.PagedResponse;
import com.clinica.practica01.core.dto.SearchParams;
import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.feature.appointment.dto.AppointmentRequest;
import com.clinica.practica01.feature.appointment.dto.AppointmentResponse;
import com.clinica.practica01.feature.appointment.dto.CompleteAppointmentRequest;
import com.clinica.practica01.feature.appointment.dto.RescheduleRequest;
import com.clinica.practica01.feature.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment", description = "Citas: reserva y maquina de estados "
        + "(PENDING -> CONFIRMED/REJECTED/RESCHEDULED -> COMPLETED/NO_SHOW/CANCELLED). "
        + "Cada cambio emite notificacion WebSocket.")
public class AppointmentController {

    private static final String PREFIX = "Appointment";

    private final AppointmentService service;
    private final PermissionChecker permissions;

    @Operation(summary = "Reservar cita (PENDING)",
            description = "Reserva una franja libre y futura. El paciente sale del token, "
                    + "o de patientId si lo reserva un admin. Notifica al medico. Requiere Appointment:Create.")
    @PostMapping
    public ResponseEntity<AppointmentResponse> book(@Valid @RequestBody AppointmentRequest request,
                                                    Authentication auth) {
        permissions.require(PREFIX + ":Create");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.book(request, auth.getName()));
    }

    @Operation(summary = "Listar citas (paginado)", description = "Requiere Appointment:List.")
    @GetMapping
    public ResponseEntity<PagedResponse<AppointmentResponse>> list(SearchParams params) {
        permissions.require(PREFIX + ":List");
        return ResponseEntity.ok(service.search(params));
    }

    @Operation(summary = "Obtener cita por id", description = "Requiere Appointment:Read.")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getById(@PathVariable UUID id) {
        permissions.require(PREFIX + ":Read");
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Confirmar cita",
            description = "PENDING -> CONFIRMED. Notifica al paciente. Medico/admin (Appointment:Update).")
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<AppointmentResponse> confirm(@PathVariable UUID id) {
        permissions.require(PREFIX + ":Update");
        return ResponseEntity.ok(service.confirm(id));
    }

    @Operation(summary = "Rechazar cita",
            description = "PENDING -> REJECTED, libera la franja. Notifica al paciente. (Appointment:Update).")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<AppointmentResponse> reject(@PathVariable UUID id) {
        permissions.require(PREFIX + ":Update");
        return ResponseEntity.ok(service.reject(id));
    }

    @Operation(summary = "Reprogramar cita",
            description = "Mueve la cita a otra franja -> RESCHEDULED (el paciente debe aceptar). (Appointment:Update).")
    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> reschedule(@PathVariable UUID id,
                                                          @Valid @RequestBody RescheduleRequest request) {
        permissions.require(PREFIX + ":Update");
        return ResponseEntity.ok(service.reschedule(id, request.getNewScheduleId()));
    }

    @Operation(summary = "Completar cita + observaciones",
            description = "CONFIRMED -> COMPLETED. El medico envia sus observaciones (diagnostico, "
                    + "tratamiento, notas) que se guardan en la historia clinica del paciente. (Appointment:Update).")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponse> complete(@PathVariable UUID id,
                                                        @RequestBody CompleteAppointmentRequest request,
                                                        Authentication auth) {
        permissions.require(PREFIX + ":Update");
        return ResponseEntity.ok(service.complete(id, request, auth.getName()));
    }

    @Operation(summary = "Marcar inasistencia",
            description = "CONFIRMED -> NO_SHOW. Medico (Appointment:Update).")
    @PatchMapping("/{id}/no-show")
    public ResponseEntity<AppointmentResponse> noShow(@PathVariable UUID id) {
        permissions.require(PREFIX + ":Update");
        return ResponseEntity.ok(service.noShow(id));
    }

    @Operation(summary = "Mis citas",
            description = "Las citas del usuario autenticado: como paciente (las suyas) o como medico (su agenda).")
    @GetMapping("/mine")
    public ResponseEntity<List<AppointmentResponse>> mine(Authentication auth) {
        return ResponseEntity.ok(service.mine(auth.getName()));
    }

    @Operation(summary = "Aceptar reprogramacion",
            description = "RESCHEDULED -> CONFIRMED. Lo hace el paciente dueño (o admin). Notifica al medico.")
    @PatchMapping("/{id}/accept")
    public ResponseEntity<AppointmentResponse> accept(@PathVariable UUID id, Authentication auth) {
        return ResponseEntity.ok(service.accept(id, auth.getName()));
    }

    @Operation(summary = "Cancelar cita",
            description = "Cancela y libera la franja. Paciente dueño o admin. Notifica a la contraparte.")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable UUID id, Authentication auth) {
        return ResponseEntity.ok(service.cancel(id, auth.getName()));
    }
}
