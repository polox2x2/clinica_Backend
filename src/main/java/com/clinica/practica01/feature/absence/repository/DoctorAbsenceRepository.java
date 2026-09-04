package com.clinica.practica01.feature.absence.repository;

import com.clinica.practica01.core.repository.BaseRepository;
import com.clinica.practica01.feature.absence.entity.DoctorAbsence;

import java.util.List;
import java.util.UUID;

public interface DoctorAbsenceRepository extends BaseRepository<DoctorAbsence> {
    List<DoctorAbsence> findByDoctorIdAndActiveTrue(UUID doctorId);
}
