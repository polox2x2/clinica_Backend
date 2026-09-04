package com.clinica.practica01.feature.doctor.mapper;

import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.feature.doctor.dto.DoctorRequest;
import com.clinica.practica01.feature.doctor.dto.DoctorResponse;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import com.clinica.practica01.feature.speciality.repository.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapea Doctor. La creacion/actualizacion de la CUENTA (User) la hace el
 * service (DoctorServiceImpl); aqui solo se mapea lo propio del medico.
 */
@Component
@RequiredArgsConstructor
public class DoctorMapper implements BaseMapper<Doctor, DoctorRequest, DoctorResponse> {

    private final SpecialityRepository specialityRepository;

    @Override
    public Doctor toEntity(DoctorRequest r) {
        Doctor d = Doctor.builder().cmp(r.getCmp()).build();
        applySpeciality(d, r);
        return d;
    }

    @Override
    public void updateEntity(Doctor e, DoctorRequest r) {
        e.setCmp(r.getCmp());
        applySpeciality(e, r);
    }

    @Override
    public DoctorResponse toResponse(Doctor e) {
        DoctorResponse res = new DoctorResponse();
        res.setCmp(e.getCmp());
        if (e.getSpeciality() != null) {
            res.setSpecialityId(e.getSpeciality().getId());
            res.setSpecialityName(e.getSpeciality().getName());
        }
        if (e.getUser() != null) {
            res.setUserId(e.getUser().getId());
            res.setUsername(e.getUser().getUsername());
            res.setFirstName(e.getUser().getFirstName());
            res.setLastName(e.getUser().getLastName());
            res.setEmail(e.getUser().getEmail());
        }
        return res;
    }

    private void applySpeciality(Doctor d, DoctorRequest r) {
        d.setSpeciality(r.getSpecialityId() == null ? null
                : specialityRepository.findById(r.getSpecialityId()).orElse(null));
    }
}
