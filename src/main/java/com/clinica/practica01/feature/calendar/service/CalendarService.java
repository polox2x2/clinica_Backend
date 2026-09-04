package com.clinica.practica01.feature.calendar.service;

import com.clinica.practica01.feature.calendar.dto.CalendarEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CalendarService {

    /** Eventos del medico en la vista indicada (day|week|month) alrededor de 'date'. */
    List<CalendarEvent> getDoctorCalendar(UUID doctorId, String view, LocalDate date);

    /** Franjas libres de un dia (opcionalmente de un medico) para recepcion/reserva. */
    List<CalendarEvent> getFreeSlots(LocalDate date, UUID doctorId);

    /** Citas de hoy de un medico (franjas ocupadas). */
    List<CalendarEvent> getToday(UUID doctorId);
}
