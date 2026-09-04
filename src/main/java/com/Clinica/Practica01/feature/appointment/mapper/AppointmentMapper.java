package com.Clinica.Practica01.feature.appointment.mapper;

import com.Clinica.Practica01.core.mapper.BaseMapper;
import com.Clinica.Practica01.feature.appointment.dto.AppointmentRequest;
import com.Clinica.Practica01.feature.appointment.dto.AppointmentResponse;
import com.Clinica.Practica01.feature.appointment.entity.Appointment;
import com.Clinica.Practica01.feature.doctor.entity.Doctor;
import com.Clinica.Practica01.feature.schedule.entity.Schedule;
import org.springframework.stereotype.Component;

/**
 * Mapea Appointment. La reserva y transiciones de estado las maneja el service.
 */
@Component
public class AppointmentMapper implements BaseMapper<Appointment, AppointmentRequest, AppointmentResponse> {

    @Override
    public Appointment toEntity(AppointmentRequest r) {
        return Appointment.builder().notes(r.getNotes()).build();
    }

    @Override
    public void updateEntity(Appointment e, AppointmentRequest r) {
        e.setNotes(r.getNotes());
    }

    @Override
    public AppointmentResponse toResponse(Appointment e) {
        AppointmentResponse res = new AppointmentResponse();
        res.setStatus(e.getStatus());
        res.setNotes(e.getNotes());
        if (e.getPatient() != null) {
            res.setPatientId(e.getPatient().getId());
            if (e.getPatient().getUser() != null) {
                res.setPatientName(e.getPatient().getUser().getFirstName() + " "
                        + e.getPatient().getUser().getLastName());
            }
        }
        Schedule s = e.getSchedule();
        if (s != null) {
            res.setScheduleId(s.getId());
            res.setDate(s.getAvailableDate());
            res.setStartTime(s.getStartTime());
            res.setEndTime(s.getEndTime());
            Doctor d = s.getDoctor();
            if (d != null) {
                res.setDoctorId(d.getId());
                if (d.getUser() != null) {
                    res.setDoctorName(d.getUser().getFirstName() + " " + d.getUser().getLastName());
                }
            }
        }
        return res;
    }
}
