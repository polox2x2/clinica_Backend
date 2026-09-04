package com.clinica.practica01.feature.availability.service;

import com.clinica.practica01.core.service.AbstractCrudService;
import com.clinica.practica01.feature.availability.dto.DoctorAvailabilityRequest;
import com.clinica.practica01.feature.availability.dto.DoctorAvailabilityResponse;
import com.clinica.practica01.feature.availability.entity.DoctorAvailability;
import com.clinica.practica01.feature.availability.mapper.DoctorAvailabilityMapper;
import com.clinica.practica01.feature.availability.repository.DoctorAvailabilityRepository;
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
