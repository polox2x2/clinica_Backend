package com.Clinica.Practica01.feature.absence.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.absence.entity.DoctorAbsence;

import java.util.List;
import java.util.UUID;

public interface DoctorAbsenceRepository extends BaseRepository<DoctorAbsence> {
    List<DoctorAbsence> findByDoctorIdAndActiveTrue(UUID doctorId);
}
