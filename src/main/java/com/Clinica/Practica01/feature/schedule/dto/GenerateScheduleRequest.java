package com.Clinica.Practica01.feature.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/** Genera los bloques de citas de un medico en un rango de fechas, segun su plantilla. */
@Data
public class GenerateScheduleRequest {
    @NotNull
    private UUID doctorId;
    @NotNull
    private LocalDate fromDate;
    @NotNull
    private LocalDate toDate;
}
