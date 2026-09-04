package com.clinica.practica01.feature.absence.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class DoctorAbsenceRequest {
    @NotNull
    private UUID doctorId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    private String reason;
}
