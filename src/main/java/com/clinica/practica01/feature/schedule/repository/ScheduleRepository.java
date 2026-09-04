package com.clinica.practica01.feature.schedule.repository;

import com.clinica.practica01.core.repository.BaseRepository;
import com.clinica.practica01.feature.schedule.entity.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends BaseRepository<Schedule> {

    List<Schedule> findByDoctorIdAndActiveTrue(UUID doctorId);

    List<Schedule> findByDoctorIdAndBookedFalseAndActiveTrue(UUID doctorId);

    boolean existsByDoctorIdAndAvailableDateAndStartTimeAndActiveTrue(
            UUID doctorId, LocalDate availableDate, LocalTime startTime);

    // Para el calendario (rango de fechas) y disponibilidad del dia
    List<Schedule> findByDoctorIdAndAvailableDateBetweenAndActiveTrue(
            UUID doctorId, LocalDate from, LocalDate to);

    List<Schedule> findByDoctorIdAndAvailableDateAndActiveTrue(UUID doctorId, LocalDate date);

    List<Schedule> findByAvailableDateAndBookedFalseAndActiveTrue(LocalDate date);
}
