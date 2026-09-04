package com.clinica.practica01.feature.availability.mapper;

import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.feature.availability.dto.DoctorAvailabilityRequest;
import com.clinica.practica01.feature.availability.dto.DoctorAvailabilityResponse;
import com.clinica.practica01.feature.availability.entity.DoctorAvailability;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorAvailabilityMapper
        implements BaseMapper<DoctorAvailability, DoctorAvailabilityRequest, DoctorAvailabilityResponse> {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorAvailability toEntity(DoctorAvailabilityRequest r) {
        DoctorAvailability a = DoctorAvailability.builder()
                .dayOfWeek(r.getDayOfWeek())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .slotDurationMinutes(r.getSlotDurationMinutes())
                .build();
        a.setDoctor(doctorRepository.findById(r.getDoctorId()).orElse(null));
        return a;
    }

    @Override
    public void updateEntity(DoctorAvailability e, DoctorAvailabilityRequest r) {
        e.setDayOfWeek(r.getDayOfWeek());
        e.setStartTime(r.getStartTime());
        e.setEndTime(r.getEndTime());
        e.setSlotDurationMinutes(r.getSlotDurationMinutes());
        e.setDoctor(doctorRepository.findById(r.getDoctorId()).orElse(null));
    }

    @Override
    public DoctorAvailabilityResponse toResponse(DoctorAvailability e) {
        DoctorAvailabilityResponse res = new DoctorAvailabilityResponse();
        res.setDayOfWeek(e.getDayOfWeek());
        res.setStartTime(e.getStartTime());
        res.setEndTime(e.getEndTime());
        res.setSlotDurationMinutes(e.getSlotDurationMinutes());
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
