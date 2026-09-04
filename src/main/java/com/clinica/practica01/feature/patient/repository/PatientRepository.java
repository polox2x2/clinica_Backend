package com.clinica.practica01.feature.patient.repository;

import com.clinica.practica01.core.repository.BaseRepository;
import com.clinica.practica01.feature.patient.entity.Patient;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends BaseRepository<Patient> {
    Optional<Patient> findByUserId(UUID userId);
}
