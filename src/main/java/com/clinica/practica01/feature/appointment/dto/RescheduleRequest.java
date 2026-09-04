package com.clinica.practica01.feature.appointment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RescheduleRequest {
    @NotNull
    private UUID newScheduleId;
}
