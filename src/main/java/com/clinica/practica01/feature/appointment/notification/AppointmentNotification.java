package com.clinica.practica01.feature.appointment.notification;

import com.clinica.practica01.feature.appointment.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/** Evento que se empuja por WebSocket al cambiar una cita. */
@Data
@AllArgsConstructor
public class AppointmentNotification {
    private String type;
    private UUID appointmentId;
    private AppointmentStatus status;
    private String message;
}
