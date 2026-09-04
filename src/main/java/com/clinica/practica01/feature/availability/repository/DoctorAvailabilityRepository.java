package com.clinica.practica01.feature.availability.repository;

import com.clinica.practica01.core.repository.BaseRepository;
import com.clinica.practica01.feature.availability.entity.DoctorAvailability;

import java.util.List;
import java.util.UUID;

public interface DoctorAvailabilityRepository extends BaseRepository<DoctorAvailability> {
    List<DoctorAvailability> findByDoctorIdAndActiveTrue(UUID doctorId);
}
