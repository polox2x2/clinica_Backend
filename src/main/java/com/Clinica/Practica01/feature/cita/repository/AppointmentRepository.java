package com.Clinica.Practica01.feature.cita.repository;

import com.Clinica.Practica01.feature.cita.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
