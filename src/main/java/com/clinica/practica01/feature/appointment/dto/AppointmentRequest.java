package com.clinica.practica01.feature.appointment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AppointmentRequest {
    @NotNull
    private UUID scheduleId;
    // Opcional: si lo manda un admin/recepcion, la cita es para este paciente.
    // Si viene null, se usa el paciente del usuario autenticado.
    private UUID patientId;
    private String notes;
}
