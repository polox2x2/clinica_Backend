package com.Clinica.Practica01.feature.medicalrecord.dto;

import com.Clinica.Practica01.core.dto.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicalRecordEntryResponse extends BaseResponse {
    private UUID doctorId;
    private String doctorName;
    private UUID appointmentId;
    private String reason;
    private String diagnosis;
    private String treatment;
    private String notes;
}
