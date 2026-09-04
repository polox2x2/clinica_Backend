package com.Clinica.Practica01.feature.schedule.service;

import com.Clinica.Practica01.core.service.CrudService;
import com.Clinica.Practica01.feature.schedule.dto.GenerateScheduleRequest;
import com.Clinica.Practica01.feature.schedule.dto.GenerateScheduleResponse;
import com.Clinica.Practica01.feature.schedule.dto.ScheduleRequest;
import com.Clinica.Practica01.feature.schedule.dto.ScheduleResponse;

public interface ScheduleService extends CrudService<ScheduleRequest, ScheduleResponse> {

    /** Genera los bloques de un medico en el rango, segun su plantilla semanal. */
    GenerateScheduleResponse generate(GenerateScheduleRequest request);
}
