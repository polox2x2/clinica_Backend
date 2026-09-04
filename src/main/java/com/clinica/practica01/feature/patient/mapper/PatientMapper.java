package com.clinica.practica01.feature.patient.mapper;

import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.feature.patient.dto.PatientRequest;
import com.clinica.practica01.feature.patient.dto.PatientResponse;
import com.clinica.practica01.feature.patient.entity.Patient;
import org.springframework.stereotype.Component;

/**
 * Mapea Patient. La cuenta (User) la gestiona el service (PatientServiceImpl).
 */
@Component
public class PatientMapper implements BaseMapper<Patient, PatientRequest, PatientResponse> {

    @Override
    public Patient toEntity(PatientRequest r) {
        return Patient.builder()
                .documentId(r.getDocumentId())
                .dateOfBirth(r.getDateOfBirth())
                .phone(r.getPhone())
                .build();
    }

    @Override
    public void updateEntity(Patient e, PatientRequest r) {
        e.setDocumentId(r.getDocumentId());
        e.setDateOfBirth(r.getDateOfBirth());
        e.setPhone(r.getPhone());
    }

    @Override
    public PatientResponse toResponse(Patient e) {
        PatientResponse res = new PatientResponse();
        res.setDocumentId(e.getDocumentId());
        res.setDateOfBirth(e.getDateOfBirth());
        res.setPhone(e.getPhone());
        if (e.getUser() != null) {
            res.setUserId(e.getUser().getId());
            res.setUsername(e.getUser().getUsername());
            res.setFirstName(e.getUser().getFirstName());
            res.setLastName(e.getUser().getLastName());
            res.setEmail(e.getUser().getEmail());
        }
        return res;
    }
}
