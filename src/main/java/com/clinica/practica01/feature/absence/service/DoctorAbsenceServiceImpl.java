package com.clinica.practica01.feature.absence.service;

import com.clinica.practica01.core.service.AbstractCrudService;
import com.clinica.practica01.feature.absence.dto.DoctorAbsenceRequest;
import com.clinica.practica01.feature.absence.dto.DoctorAbsenceResponse;
import com.clinica.practica01.feature.absence.entity.DoctorAbsence;
import com.clinica.practica01.feature.absence.mapper.DoctorAbsenceMapper;
import com.clinica.practica01.feature.absence.repository.DoctorAbsenceRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorAbsenceServiceImpl
        extends AbstractCrudService<DoctorAbsence, DoctorAbsenceRequest, DoctorAbsenceResponse>
        implements DoctorAbsenceService {

    public DoctorAbsenceServiceImpl(DoctorAbsenceRepository repository, DoctorAbsenceMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String resourceName() {
        return "Absence";
    }
}
