package com.Clinica.Practica01.feature.doctor.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.doctor.entity.Doctor;

import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends BaseRepository<Doctor> {
    Optional<Doctor> findByUserId(UUID userId);
}
