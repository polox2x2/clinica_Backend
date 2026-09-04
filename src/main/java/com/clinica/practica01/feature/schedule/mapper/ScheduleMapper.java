package com.clinica.practica01.feature.schedule.mapper;

import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import com.clinica.practica01.feature.schedule.dto.ScheduleRequest;
import com.clinica.practica01.feature.schedule.dto.ScheduleResponse;
import com.clinica.practica01.feature.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleMapper implements BaseMapper<Schedule, ScheduleRequest, ScheduleResponse> {

    private final DoctorRepository doctorRepository;

    @Override
    public Schedule toEntity(ScheduleRequest r) {
        Schedule s = Schedule.builder()
                .availableDate(r.getAvailableDate())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .booked(false)
                .build();
        s.setDoctor(doctorRepository.findById(r.getDoctorId()).orElse(null));
        return s;
    }

    @Override
    public void updateEntity(Schedule e, ScheduleRequest r) {
        e.setAvailableDate(r.getAvailableDate());
        e.setStartTime(r.getStartTime());
        e.setEndTime(r.getEndTime());
        e.setDoctor(doctorRepository.findById(r.getDoctorId()).orElse(null));
    }

    @Override
    public ScheduleResponse toResponse(Schedule e) {
        ScheduleResponse res = new ScheduleResponse();
        res.setAvailableDate(e.getAvailableDate());
        res.setStartTime(e.getStartTime());
        res.setEndTime(e.getEndTime());
        res.setBooked(e.isBooked());
        if (e.getDoctor() != null) {
            res.setDoctorId(e.getDoctor().getId());
            if (e.getDoctor().getUser() != null) {
                res.setDoctorName(e.getDoctor().getUser().getFirstName() + " "
                        + e.getDoctor().getUser().getLastName());
            }
        }
        return res;
    }
}
