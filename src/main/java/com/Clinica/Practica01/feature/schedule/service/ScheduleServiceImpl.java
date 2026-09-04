package com.Clinica.Practica01.feature.schedule.service;

import com.Clinica.Practica01.core.exception.ResourceNotFoundException;
import com.Clinica.Practica01.core.service.AbstractCrudService;
import com.Clinica.Practica01.feature.absence.entity.DoctorAbsence;
import com.Clinica.Practica01.feature.absence.repository.DoctorAbsenceRepository;
import com.Clinica.Practica01.feature.availability.entity.DoctorAvailability;
import com.Clinica.Practica01.feature.availability.repository.DoctorAvailabilityRepository;
import com.Clinica.Practica01.feature.calendar.notification.CalendarNotifier;
import com.Clinica.Practica01.feature.doctor.entity.Doctor;
import com.Clinica.Practica01.feature.doctor.repository.DoctorRepository;
import com.Clinica.Practica01.feature.schedule.dto.GenerateScheduleRequest;
import com.Clinica.Practica01.feature.schedule.dto.GenerateScheduleResponse;
import com.Clinica.Practica01.feature.schedule.dto.ScheduleRequest;
import com.Clinica.Practica01.feature.schedule.dto.ScheduleResponse;
import com.Clinica.Practica01.feature.schedule.entity.Schedule;
import com.Clinica.Practica01.feature.schedule.mapper.ScheduleMapper;
import com.Clinica.Practica01.feature.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl
        extends AbstractCrudService<Schedule, ScheduleRequest, ScheduleResponse>
        implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorAbsenceRepository absenceRepository;
    private final CalendarNotifier calendarNotifier;

    public ScheduleServiceImpl(ScheduleRepository repository, ScheduleMapper mapper,
                               DoctorRepository doctorRepository,
                               DoctorAvailabilityRepository availabilityRepository,
                               DoctorAbsenceRepository absenceRepository,
                               CalendarNotifier calendarNotifier) {
        super(repository, mapper);
        this.scheduleRepository = repository;
        this.doctorRepository = doctorRepository;
        this.availabilityRepository = availabilityRepository;
        this.absenceRepository = absenceRepository;
        this.calendarNotifier = calendarNotifier;
    }

    @Override
    protected String resourceName() {
        return "Schedule";
    }

    @Override
    @Transactional
    public GenerateScheduleResponse generate(GenerateScheduleRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Medico no encontrado"));

        Map<DayOfWeek, DoctorAvailability> byDay = new EnumMap<>(DayOfWeek.class);
        for (DoctorAvailability a : availabilityRepository.findByDoctorIdAndActiveTrue(doctor.getId())) {
            byDay.put(a.getDayOfWeek(), a);
        }
        List<DoctorAbsence> absences = absenceRepository.findByDoctorIdAndActiveTrue(doctor.getId());

        int generated = 0;
        for (LocalDate date = request.getFromDate();
             !date.isAfter(request.getToDate());
             date = date.plusDays(1)) {

            if (isAbsent(date, absences)) {
                continue;
            }
            DoctorAvailability avail = byDay.get(date.getDayOfWeek());
            if (avail == null) {
                continue; // no trabaja ese dia
            }
            generated += generateDay(doctor, date, avail);
        }
        if (generated > 0) {
            calendarNotifier.calendarChanged(doctor.getId(), "SLOTS_GENERATED");
        }
        return new GenerateScheduleResponse(generated);
    }

    private int generateDay(Doctor doctor, LocalDate date, DoctorAvailability avail) {
        int count = 0;
        LocalTime t = avail.getStartTime();
        int dur = avail.getSlotDurationMinutes();
        while (!t.plusMinutes(dur).isAfter(avail.getEndTime())) {
            LocalTime end = t.plusMinutes(dur);
            boolean exists = scheduleRepository
                    .existsByDoctorIdAndAvailableDateAndStartTimeAndActiveTrue(doctor.getId(), date, t);
            if (!exists) {
                Schedule slot = Schedule.builder()
                        .doctor(doctor)
                        .availableDate(date)
                        .startTime(t)
                        .endTime(end)
                        .booked(false)
                        .build();
                slot.setActive(true);
                scheduleRepository.save(slot);
                count++;
            }
            t = end;
        }
        return count;
    }

    private boolean isAbsent(LocalDate date, List<DoctorAbsence> absences) {
        return absences.stream().anyMatch(a ->
                !date.isBefore(a.getStartDate()) && !date.isAfter(a.getEndDate()));
    }
}
