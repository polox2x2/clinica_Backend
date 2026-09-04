package com.clinica.practica01.feature.availability.service;

import com.clinica.practica01.core.service.CrudService;
import com.clinica.practica01.feature.availability.dto.DoctorAvailabilityRequest;
import com.clinica.practica01.feature.availability.dto.DoctorAvailabilityResponse;

public interface DoctorAvailabilityService
        extends CrudService<DoctorAvailabilityRequest, DoctorAvailabilityResponse> {
}
