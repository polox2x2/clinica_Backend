package com.clinica.practica01.feature.appointment.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import com.clinica.practica01.feature.appointment.entity.AppointmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentResponse extends BaseResponse {
    private UUID patientId;
    private String patientName;
    private UUID doctorId;
    private String doctorName;
    private UUID scheduleId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus status;
    private String notes;
}
