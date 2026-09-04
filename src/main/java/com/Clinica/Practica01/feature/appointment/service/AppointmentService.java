package com.Clinica.Practica01.feature.appointment.service;

import com.Clinica.Practica01.core.service.CrudService;
import com.Clinica.Practica01.feature.appointment.dto.AppointmentRequest;
import com.Clinica.Practica01.feature.appointment.dto.AppointmentResponse;
import com.Clinica.Practica01.feature.appointment.dto.CompleteAppointmentRequest;

import java.util.List;
import java.util.UUID;

public interface AppointmentService extends CrudService<AppointmentRequest, AppointmentResponse> {

    /** Reserva (PENDING). El paciente sale del token, o del patientId si es admin. */
    AppointmentResponse book(AppointmentRequest request, String username);

    /** Medico/admin confirma (PENDING -> CONFIRMED). */
    AppointmentResponse confirm(UUID id);

    /** Medico/admin rechaza (PENDING -> REJECTED, libera la franja). */
    AppointmentResponse reject(UUID id);

    /** Medico/admin reprograma a otra franja (-> RESCHEDULED, espera al paciente). */
    AppointmentResponse reschedule(UUID id, UUID newScheduleId);

    /** El paciente acepta la reprogramacion (RESCHEDULED -> CONFIRMED). */
    AppointmentResponse accept(UUID id, String username);

    /** Cancela (paciente dueño o admin). Libera la franja. */
    AppointmentResponse cancel(UUID id, String username);

    /**
     * El medico completa la cita (CONFIRMED -> COMPLETED) y registra sus
     * observaciones, que se guardan como una entrada en la historia clinica.
     */
    AppointmentResponse complete(UUID id, CompleteAppointmentRequest request, String username);

    /** El medico marca inasistencia (CONFIRMED -> NO_SHOW). */
    AppointmentResponse noShow(UUID id);

    /** Mis citas: como paciente (las mias) o como medico (las de mi agenda). */
    List<AppointmentResponse> mine(String username);
}
