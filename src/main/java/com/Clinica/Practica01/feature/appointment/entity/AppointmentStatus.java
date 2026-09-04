package com.Clinica.Practica01.feature.appointment.entity;

public enum AppointmentStatus {
    PENDING,      // reservada por el paciente, espera confirmacion
    CONFIRMED,    // confirmada por el medico/admin
    REJECTED,     // rechazada por el medico/admin
    RESCHEDULED,  // reprogramada, espera que el paciente acepte
    CANCELLED,    // cancelada (por paciente o admin)
    COMPLETED,    // atendida
    NO_SHOW       // el paciente no asistio
}
