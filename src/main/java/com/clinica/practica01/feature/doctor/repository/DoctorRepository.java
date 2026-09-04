package com.clinica.practica01.feature.doctor.repository;

import com.clinica.practica01.core.repository.BaseRepository;
import com.clinica.practica01.feature.doctor.entity.Doctor;

import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends BaseRepository<Doctor> {
    Optional<Doctor> findByUserId(UUID userId);
}
