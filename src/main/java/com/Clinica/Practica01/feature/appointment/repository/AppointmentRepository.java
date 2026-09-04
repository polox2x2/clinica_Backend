package com.Clinica.Practica01.feature.appointment.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.appointment.entity.Appointment;
import com.Clinica.Practica01.feature.appointment.entity.AppointmentStatus;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends BaseRepository<Appointment> {
    List<Appointment> findByPatientIdAndActiveTrue(UUID patientId);

    List<Appointment> findBySchedule_Doctor_IdAndActiveTrue(UUID doctorId);

    // Citas que "ocupan" un conjunto de franjas (para el calendario)
    List<Appointment> findByScheduleIdInAndStatusInAndActiveTrue(
            Collection<UUID> scheduleIds, Collection<AppointmentStatus> statuses);
}
