package com.clinica.practica01.feature.calendar.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/** Evento del calendario (una franja, libre u ocupada por una cita). */
@Data
@Builder
public class CalendarEvent {
    private UUID scheduleId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean booked;
    // "FREE" si esta libre; si no, el estado de la cita (PENDING, CONFIRMED...)
    private String status;
    private UUID appointmentId;
    private UUID patientId;
    private String patientName;
}
