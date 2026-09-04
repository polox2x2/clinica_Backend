package com.clinica.practica01.feature.calendar.service;

import com.clinica.practica01.feature.appointment.entity.Appointment;
import com.clinica.practica01.feature.appointment.entity.AppointmentStatus;
import com.clinica.practica01.feature.appointment.repository.AppointmentRepository;
import com.clinica.practica01.feature.calendar.dto.CalendarEvent;
import com.clinica.practica01.feature.schedule.entity.Schedule;
import com.clinica.practica01.feature.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    // Estados que "ocupan" una franja
    private static final List<AppointmentStatus> OCCUPYING = List.of(
            AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED, AppointmentStatus.RESCHEDULED,
            AppointmentStatus.COMPLETED, AppointmentStatus.NO_SHOW);

    private final ScheduleRepository scheduleRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CalendarEvent> getDoctorCalendar(UUID doctorId, String view, LocalDate date) {
        LocalDate from = date;
        LocalDate to = date;
        String v = view == null ? "day" : view.toLowerCase();
        if (v.equals("week")) {
            from = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            to = from.plusDays(6);
        } else if (v.equals("month")) {
            from = date.withDayOfMonth(1);
            to = date.with(TemporalAdjusters.lastDayOfMonth());
        }
        List<Schedule> schedules =
                scheduleRepository.findByDoctorIdAndAvailableDateBetweenAndActiveTrue(doctorId, from, to);
        return toEvents(schedules);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarEvent> getFreeSlots(LocalDate date, UUID doctorId) {
        List<Schedule> free = (doctorId != null)
                ? scheduleRepository.findByDoctorIdAndAvailableDateAndActiveTrue(doctorId, date)
                        .stream().filter(s -> !s.isBooked()).toList()
                : scheduleRepository.findByAvailableDateAndBookedFalseAndActiveTrue(date);
        return toEvents(free);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarEvent> getToday(UUID doctorId) {
        List<Schedule> schedules = scheduleRepository
                .findByDoctorIdAndAvailableDateAndActiveTrue(doctorId, LocalDate.now(ZoneId.systemDefault()));
        return toEvents(schedules).stream()
                .filter(e -> e.getAppointmentId() != null)
                .toList();
    }

    private List<CalendarEvent> toEvents(List<Schedule> schedules) {
        if (schedules.isEmpty()) {
            return List.of();
        }
        List<UUID> ids = schedules.stream().map(Schedule::getId).toList();
        Map<UUID, Appointment> bySchedule = new HashMap<>();
        for (Appointment a : appointmentRepository
                .findByScheduleIdInAndStatusInAndActiveTrue(ids, OCCUPYING)) {
            bySchedule.put(a.getSchedule().getId(), a);
        }
        return schedules.stream().map(s -> toEvent(s, bySchedule.get(s.getId()))).toList();
    }

    private CalendarEvent toEvent(Schedule s, Appointment appt) {
        CalendarEvent.CalendarEventBuilder b = CalendarEvent.builder()
                .scheduleId(s.getId())
                .date(s.getAvailableDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .booked(s.isBooked());
        if (appt != null) {
            b.status(appt.getStatus().name());
            b.appointmentId(appt.getId());
            if (appt.getPatient() != null) {
                b.patientId(appt.getPatient().getId());
                if (appt.getPatient().getUser() != null) {
                    b.patientName(appt.getPatient().getUser().getFirstName() + " "
                            + appt.getPatient().getUser().getLastName());
                }
            }
        } else {
            b.status("FREE");
        }
        return b.build();
    }
}
