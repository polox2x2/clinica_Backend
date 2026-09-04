package com.Clinica.Practica01.feature.availability.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.availability.entity.DoctorAvailability;

import java.util.List;
import java.util.UUID;

public interface DoctorAvailabilityRepository extends BaseRepository<DoctorAvailability> {
    List<DoctorAvailability> findByDoctorIdAndActiveTrue(UUID doctorId);
}
