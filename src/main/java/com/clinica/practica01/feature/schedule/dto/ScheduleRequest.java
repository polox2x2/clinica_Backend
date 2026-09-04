package com.clinica.practica01.feature.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ScheduleRequest {
    @NotNull
    private UUID doctorId;
    @NotNull
    private LocalDate availableDate;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
}
