package com.clinica.practica01.feature.absence.mapper;

import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.feature.absence.dto.DoctorAbsenceRequest;
import com.clinica.practica01.feature.absence.dto.DoctorAbsenceResponse;
import com.clinica.practica01.feature.absence.entity.DoctorAbsence;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorAbsenceMapper
        implements BaseMapper<DoctorAbsence, DoctorAbsenceRequest, DoctorAbsenceResponse> {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorAbsence toEntity(DoctorAbsenceRequest r) {
        DoctorAbsence a = DoctorAbsence.builder()
                .startDate(r.getStartDate())
                .endDate(r.getEndDate())
                .reason(r.getReason())
                .build();
        a.setDoctor(doctorRepository.findById(r.getDoctorId()).orElse(null));
        return a;
    }

    @Override
    public void updateEntity(DoctorAbsence e, DoctorAbsenceRequest r) {
        e.setStartDate(r.getStartDate());
        e.setEndDate(r.getEndDate());
        e.setReason(r.getReason());
        e.setDoctor(doctorRepository.findById(r.getDoctorId()).orElse(null));
    }

    @Override
    public DoctorAbsenceResponse toResponse(DoctorAbsence e) {
        DoctorAbsenceResponse res = new DoctorAbsenceResponse();
        res.setStartDate(e.getStartDate());
        res.setEndDate(e.getEndDate());
        res.setReason(e.getReason());
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
