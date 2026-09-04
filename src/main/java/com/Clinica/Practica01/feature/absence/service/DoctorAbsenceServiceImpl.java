package com.Clinica.Practica01.feature.absence.service;

import com.Clinica.Practica01.core.service.AbstractCrudService;
import com.Clinica.Practica01.feature.absence.dto.DoctorAbsenceRequest;
import com.Clinica.Practica01.feature.absence.dto.DoctorAbsenceResponse;
import com.Clinica.Practica01.feature.absence.entity.DoctorAbsence;
import com.Clinica.Practica01.feature.absence.mapper.DoctorAbsenceMapper;
import com.Clinica.Practica01.feature.absence.repository.DoctorAbsenceRepository;
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
