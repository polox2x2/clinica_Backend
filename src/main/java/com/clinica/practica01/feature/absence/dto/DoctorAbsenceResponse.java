package com.clinica.practica01.feature.absence.dto;

import com.clinica.practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorAbsenceResponse extends BaseResponse {
    private UUID doctorId;
    private String doctorName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
