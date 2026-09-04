package com.Clinica.Practica01.feature.patient.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.patient.entity.Patient;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends BaseRepository<Patient> {
    Optional<Patient> findByUserId(UUID userId);
}
