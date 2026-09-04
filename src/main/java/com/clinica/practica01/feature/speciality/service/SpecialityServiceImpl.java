package com.clinica.practica01.feature.speciality.service;

import com.clinica.practica01.core.service.AbstractCrudService;
import com.clinica.practica01.feature.speciality.dto.SpecialityRequest;
import com.clinica.practica01.feature.speciality.dto.SpecialityResponse;
import com.clinica.practica01.feature.speciality.entity.Speciality;
import com.clinica.practica01.feature.speciality.mapper.SpecialityMapper;
import com.clinica.practica01.feature.speciality.repository.SpecialityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialityServiceImpl
        extends AbstractCrudService<Speciality, SpecialityRequest, SpecialityResponse>
        implements SpecialityService {

    public SpecialityServiceImpl(SpecialityRepository repository, SpecialityMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String resourceName() {
        return "Speciality";
    }

    @Override
    protected List<String> searchableFields() {
        return List.of("name");
    }
}
