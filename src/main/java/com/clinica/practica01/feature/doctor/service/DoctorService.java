package com.clinica.practica01.feature.doctor.service;

import com.clinica.practica01.core.service.CrudService;
import com.clinica.practica01.feature.doctor.dto.DoctorRequest;
import com.clinica.practica01.feature.doctor.dto.DoctorResponse;

public interface DoctorService extends CrudService<DoctorRequest, DoctorResponse> {
    DoctorResponse getMe(String username);
}
