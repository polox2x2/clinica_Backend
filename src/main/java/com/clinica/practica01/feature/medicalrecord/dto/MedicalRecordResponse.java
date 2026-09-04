package com.clinica.practica01.feature.medicalrecord.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/** Historia clinica completa de un paciente. */
@Data
@Builder
public class MedicalRecordResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private String allergies;
    private String bloodType;
    private List<MedicalRecordEntryResponse> entries;
}
