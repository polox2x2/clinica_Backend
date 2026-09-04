package com.Clinica.Practica01.feature.medicalrecord.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.medicalrecord.entity.MedicalRecord;

import java.util.Optional;
import java.util.UUID;

public interface MedicalRecordRepository extends BaseRepository<MedicalRecord> {
    Optional<MedicalRecord> findByPatientId(UUID patientId);
}
