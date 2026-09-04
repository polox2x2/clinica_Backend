package com.Clinica.Practica01.feature.medicalrecord.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.medicalrecord.entity.MedicalRecordEntry;

import java.util.List;
import java.util.UUID;

public interface MedicalRecordEntryRepository extends BaseRepository<MedicalRecordEntry> {
    List<MedicalRecordEntry> findByRecordIdAndActiveTrueOrderByCreatedAtDesc(UUID recordId);
}
