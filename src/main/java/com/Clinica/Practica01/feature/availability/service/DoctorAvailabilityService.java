package com.Clinica.Practica01.feature.availability.service;

import com.Clinica.Practica01.core.service.CrudService;
import com.Clinica.Practica01.feature.availability.dto.DoctorAvailabilityRequest;
import com.Clinica.Practica01.feature.availability.dto.DoctorAvailabilityResponse;

public interface DoctorAvailabilityService
        extends CrudService<DoctorAvailabilityRequest, DoctorAvailabilityResponse> {
}
