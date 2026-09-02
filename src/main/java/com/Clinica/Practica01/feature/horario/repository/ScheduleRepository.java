package com.Clinica.Practica01.feature.horario.repository;

import com.Clinica.Practica01.feature.horario.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
