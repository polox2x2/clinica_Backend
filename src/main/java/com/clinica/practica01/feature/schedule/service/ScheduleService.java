package com.clinica.practica01.feature.schedule.service;

import com.clinica.practica01.core.service.CrudService;
import com.clinica.practica01.feature.schedule.dto.GenerateScheduleRequest;
import com.clinica.practica01.feature.schedule.dto.GenerateScheduleResponse;
import com.clinica.practica01.feature.schedule.dto.ScheduleRequest;
import com.clinica.practica01.feature.schedule.dto.ScheduleResponse;

public interface ScheduleService extends CrudService<ScheduleRequest, ScheduleResponse> {

    /** Genera los bloques de un medico en el rango, segun su plantilla semanal. */
    GenerateScheduleResponse generate(GenerateScheduleRequest request);
}
