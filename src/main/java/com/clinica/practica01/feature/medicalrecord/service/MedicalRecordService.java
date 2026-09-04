package com.clinica.practica01.feature.medicalrecord.service;

import com.clinica.practica01.feature.medicalrecord.dto.MedicalRecordEntryRequest;
import com.clinica.practica01.feature.medicalrecord.dto.MedicalRecordEntryResponse;
import com.clinica.practica01.feature.medicalrecord.dto.MedicalRecordResponse;

import java.util.UUID;

public interface MedicalRecordService {

    /** El medico (del token) registra una atencion en la historia del paciente. */
    MedicalRecordEntryResponse addEntry(MedicalRecordEntryRequest request, String username);

    /** Historia clinica completa de un paciente (con sus atenciones). */
    MedicalRecordResponse getByPatient(UUID patientId);

    /** Mi historia clinica (paciente autenticado). */
    MedicalRecordResponse getMine(String username);
}
