package com.Clinica.Practica01.feature.medicalrecord.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MedicalRecordEntryRequest {
    @NotNull
    private UUID patientId;
    private UUID appointmentId; // opcional
    private String reason;
    private String diagnosis;
    private String treatment;
    private String notes;
}
