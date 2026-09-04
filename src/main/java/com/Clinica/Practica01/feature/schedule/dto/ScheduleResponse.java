package com.Clinica.Practica01.feature.schedule.dto;

import com.Clinica.Practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleResponse extends BaseResponse {
    private UUID doctorId;
    private String doctorName;
    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean booked;
}
