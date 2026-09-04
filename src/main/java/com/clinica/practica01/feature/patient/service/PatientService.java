package com.clinica.practica01.feature.patient.service;

import com.clinica.practica01.core.service.CrudService;
import com.clinica.practica01.feature.patient.dto.PatientRequest;
import com.clinica.practica01.feature.patient.dto.PatientResponse;
import com.clinica.practica01.feature.patient.dto.SelfPatientRequest;

public interface PatientService extends CrudService<PatientRequest, PatientResponse> {

    /** Perfil de paciente del usuario autenticado (404 si aun no lo creo). */
    PatientResponse getMe(String username);

    /** El paciente crea su propio perfil (su cuenta ya existe). */
    PatientResponse createMe(String username, SelfPatientRequest request);

    /** El paciente actualiza su propio perfil. */
    PatientResponse updateMe(String username, SelfPatientRequest request);
}
