package com.clinica.practica01.feature.calendar.controller;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.feature.calendar.dto.CalendarEvent;
import com.clinica.practica01.feature.calendar.service.CalendarService;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
@Tag(name = "Calendar", description = "Agenda tipo calendario (dia/semana/mes), franjas libres y citas de hoy. "
        + "Los cambios se emiten en vivo por WebSocket /topic/calendar/doctor/{id}")
public class CalendarController {

    private final CalendarService calendarService;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PermissionChecker permissions;

    /** Agenda de un medico (admin/recepcion). view = day|week|month. */
    @Operation(summary = "Agenda de un medico",
            description = "Eventos (franjas + estado de cita) en vista day|week|month alrededor de 'date'. Calendar:Read.")
    @GetMapping
    public ResponseEntity<List<CalendarEvent>> doctorCalendar(
            @RequestParam UUID doctorId,
            @RequestParam(defaultValue = "week") String view,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        permissions.require("Calendar:Read");
        return ResponseEntity.ok(calendarService.getDoctorCalendar(doctorId, view, orToday(date)));
    }

    /** Agenda del medico autenticado. */
    @Operation(summary = "Mi agenda (medico autenticado)",
            description = "Agenda del medico logueado en la vista indicada.")
    @GetMapping("/me")
    public ResponseEntity<List<CalendarEvent>> myCalendar(
            @RequestParam(defaultValue = "week") String view,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication auth) {
        UUID doctorId = currentDoctorId(auth.getName());
        return ResponseEntity.ok(calendarService.getDoctorCalendar(doctorId, view, orToday(date)));
    }

    /** Citas de hoy del medico autenticado. */
    @Operation(summary = "Mis citas de hoy",
            description = "Citas de hoy del medico autenticado (franjas ocupadas).")
    @GetMapping("/me/today")
    public ResponseEntity<List<CalendarEvent>> myToday(Authentication auth) {
        return ResponseEntity.ok(calendarService.getToday(currentDoctorId(auth.getName())));
    }

    /** Franjas libres de un dia (recepcion / reserva). */
    @Operation(summary = "Franjas libres de un dia",
            description = "Slots libres (opcionalmente de un medico) para recepcion / reserva. Requiere Schedule:List.")
    @GetMapping("/free-slots")
    public ResponseEntity<List<CalendarEvent>> freeSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) UUID doctorId) {
        permissions.require("Schedule:List");
        return ResponseEntity.ok(calendarService.getFreeSlots(date, doctorId));
    }

    private LocalDate orToday(LocalDate date) {
        return date != null ? date : LocalDate.now(ZoneId.systemDefault());
    }

    private UUID currentDoctorId(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException("No eres un medico", HttpStatus.FORBIDDEN));
        return doctor.getId();
    }
}
