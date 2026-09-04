package com.clinica.practica01.feature.appointment.mapper;

import com.clinica.practica01.feature.appointment.dto.AppointmentRequest;
import com.clinica.practica01.feature.appointment.entity.Appointment;
import com.clinica.practica01.feature.appointment.entity.AppointmentStatus;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import com.clinica.practica01.feature.patient.entity.Patient;
import com.clinica.practica01.feature.schedule.entity.Schedule;
import com.clinica.practica01.feature.user.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentMapperTest {

    private final AppointmentMapper mapper = new AppointmentMapper();

    @Test
    void toEntityAndUpdate() {
        AppointmentRequest req = new AppointmentRequest();
        req.setNotes("nota");
        Appointment e = mapper.toEntity(req);
        assertThat(e.getNotes()).isEqualTo("nota");

        req.setNotes("otra");
        mapper.updateEntity(e, req);
        assertThat(e.getNotes()).isEqualTo("otra");
    }

    @Test
    void toResponse_mapsScheduleDoctorAndPatient() {
        User docU = new User(); docU.setFirstName("Dr"); docU.setLastName("Who");
        Doctor d = Doctor.builder().user(docU).build(); d.setId(UUID.randomUUID());
        Schedule s = Schedule.builder().doctor(d)
                .availableDate(LocalDate.of(2026, 1, 2))
                .startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(9, 30)).build();
        s.setId(UUID.randomUUID());
        User patU = new User(); patU.setFirstName("Ana"); patU.setLastName("Gomez");
        Patient p = Patient.builder().user(patU).build(); p.setId(UUID.randomUUID());
        Appointment a = Appointment.builder().patient(p).schedule(s)
                .status(AppointmentStatus.PENDING).notes("n").build();

        var res = mapper.toResponse(a);
        assertThat(res.getStatus()).isEqualTo(AppointmentStatus.PENDING);
        assertThat(res.getDoctorName()).isEqualTo("Dr Who");
        assertThat(res.getPatientName()).isEqualTo("Ana Gomez");
        assertThat(res.getScheduleId()).isEqualTo(s.getId());
    }

    @Test
    void toResponse_withoutRelations() {
        Appointment a = Appointment.builder().status(AppointmentStatus.PENDING).build();
        assertThat(mapper.toResponse(a).getDoctorName()).isNull();
    }
}
