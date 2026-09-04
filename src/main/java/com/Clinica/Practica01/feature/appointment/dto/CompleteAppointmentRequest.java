package com.Clinica.Practica01.feature.appointment.dto;

import lombok.Data;

/** Observaciones del medico al completar la cita (se guardan en la historia clinica). */
@Data
public class CompleteAppointmentRequest {
    private String reason;
    private String diagnosis;
    private String treatment;
    private String observations;
}
