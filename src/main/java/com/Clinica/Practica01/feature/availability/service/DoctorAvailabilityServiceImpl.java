package com.Clinica.Practica01.feature.availability.service;

import com.Clinica.Practica01.core.service.AbstractCrudService;
import com.Clinica.Practica01.feature.availability.dto.DoctorAvailabilityRequest;
import com.Clinica.Practica01.feature.availability.dto.DoctorAvailabilityResponse;
import com.Clinica.Practica01.feature.availability.entity.DoctorAvailability;
import com.Clinica.Practica01.feature.availability.mapper.DoctorAvailabilityMapper;
import com.Clinica.Practica01.feature.availability.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorAvailabilityServiceImpl
        extends AbstractCrudService<DoctorAvailability, DoctorAvailabilityRequest, DoctorAvailabilityResponse>
        implements DoctorAvailabilityService {

    public DoctorAvailabilityServiceImpl(DoctorAvailabilityRepository repository,
                                         DoctorAvailabilityMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String resourceName() {
        return "Availability";
    }
}
