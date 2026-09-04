package com.clinica.practica01.feature.medicalrecord.repository;

import com.clinica.practica01.core.repository.BaseRepository;
import com.clinica.practica01.feature.medicalrecord.entity.MedicalRecord;

import java.util.Optional;
import java.util.UUID;

public interface MedicalRecordRepository extends BaseRepository<MedicalRecord> {
    Optional<MedicalRecord> findByPatientId(UUID patientId);
}
