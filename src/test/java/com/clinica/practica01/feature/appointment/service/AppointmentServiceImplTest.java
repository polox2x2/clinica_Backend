package com.clinica.practica01.feature.appointment.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.feature.appointment.dto.AppointmentRequest;
import com.clinica.practica01.feature.appointment.dto.AppointmentResponse;
import com.clinica.practica01.feature.appointment.dto.CompleteAppointmentRequest;
import com.clinica.practica01.feature.appointment.entity.Appointment;
import com.clinica.practica01.feature.appointment.entity.AppointmentStatus;
import com.clinica.practica01.feature.appointment.mapper.AppointmentMapper;
import com.clinica.practica01.feature.appointment.notification.AppointmentNotifier;
import com.clinica.practica01.feature.appointment.repository.AppointmentRepository;
import com.clinica.practica01.feature.calendar.notification.CalendarNotifier;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import com.clinica.practica01.feature.medicalrecord.service.MedicalRecordService;
import com.clinica.practica01.feature.patient.entity.Patient;
import com.clinica.practica01.feature.patient.repository.PatientRepository;
import com.clinica.practica01.feature.schedule.entity.Schedule;
import com.clinica.practica01.feature.schedule.repository.ScheduleRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppointmentServiceImplTest {

    @Mock AppointmentRepository appointmentRepository;
    @Mock AppointmentMapper mapper;
    @Mock ScheduleRepository scheduleRepository;
    @Mock PatientRepository patientRepository;
    @Mock UserRepository userRepository;
    @Mock DoctorRepository doctorRepository;
    @Mock AppointmentNotifier notifier;
    @Mock CalendarNotifier calendarNotifier;
    @Mock MedicalRecordService medicalRecordService;
    @Mock PermissionChecker permissions;

    AppointmentServiceImpl service;

    private final AppointmentResponse RESP = new AppointmentResponse();

    @BeforeEach
    void setUp() {
        service = new AppointmentServiceImpl(appointmentRepository, mapper, scheduleRepository,
                patientRepository, userRepository, doctorRepository, notifier, calendarNotifier,
                medicalRecordService, permissions);
        lenient().when(mapper.toResponseWithBase(any())).thenReturn(RESP);
        lenient().when(appointmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        lenient().when(scheduleRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    // ---- fixtures ----
    private User user(String username) {
        User u = new User();
        u.setId(UUID.randomUUID());
        u.setUsername(username);
        return u;
    }
    private Patient patient(User u) {
        Patient p = Patient.builder().user(u).build();
        p.setId(UUID.randomUUID());
        p.setActive(true);
        return p;
    }
    private Doctor doctor(User u) {
        Doctor d = Doctor.builder().user(u).build();
        d.setId(UUID.randomUUID());
        return d;
    }
    private Schedule futureSlot(Doctor d, boolean booked) {
        Schedule s = Schedule.builder()
                .doctor(d)
                .availableDate(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .booked(booked)
                .build();
        s.setId(UUID.randomUUID());
        s.setActive(true);
        return s;
    }
    private Appointment appt(Patient p, Schedule s, AppointmentStatus st) {
        Appointment a = Appointment.builder().patient(p).schedule(s).status(st).build();
        a.setId(UUID.randomUUID());
        a.setActive(true);
        return a;
    }

    // ---- book ----
    @Test
    void book_byUsername_createsPendingAndNotifies() {
        User pu = user("pat");
        Patient p = patient(pu);
        Doctor d = doctor(user("doc"));
        Schedule s = futureSlot(d, false);
        AppointmentRequest req = new AppointmentRequest();
        req.setScheduleId(s.getId());
        when(userRepository.findByUsername("pat")).thenReturn(Optional.of(pu));
        when(patientRepository.findByUserId(pu.getId())).thenReturn(Optional.of(p));
        when(scheduleRepository.findById(s.getId())).thenReturn(Optional.of(s));

        assertThat(service.book(req, "pat")).isSameAs(RESP);
        assertThat(s.isBooked()).isTrue();
        verify(appointmentRepository).save(any(Appointment.class));
        verify(notifier).notifyUser(anyString(), any());
    }

    @Test
    void book_withExplicitPatientId_resolvesById() {
        Patient p = patient(user("pat"));
        Schedule s = futureSlot(doctor(user("doc")), false);
        AppointmentRequest req = new AppointmentRequest();
        req.setScheduleId(s.getId());
        req.setPatientId(p.getId());
        when(patientRepository.findById(p.getId())).thenReturn(Optional.of(p));
        when(scheduleRepository.findById(s.getId())).thenReturn(Optional.of(s));

        assertThat(service.book(req, "admin")).isSameAs(RESP);
    }

    @Test
    void book_throws_whenSlotBooked() {
        Patient p = patient(user("pat"));
        Schedule s = futureSlot(doctor(user("doc")), true);
        AppointmentRequest req = new AppointmentRequest();
        req.setScheduleId(s.getId());
        req.setPatientId(p.getId());
        when(patientRepository.findById(p.getId())).thenReturn(Optional.of(p));
        when(scheduleRepository.findById(s.getId())).thenReturn(Optional.of(s));

        assertThatThrownBy(() -> service.book(req, "admin"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void book_throws_whenSlotInPast() {
        Patient p = patient(user("pat"));
        Doctor d = doctor(user("doc"));
        Schedule s = futureSlot(d, false);
        s.setAvailableDate(LocalDate.now().minusDays(1));
        AppointmentRequest req = new AppointmentRequest();
        req.setScheduleId(s.getId());
        req.setPatientId(p.getId());
        when(patientRepository.findById(p.getId())).thenReturn(Optional.of(p));
        when(scheduleRepository.findById(s.getId())).thenReturn(Optional.of(s));

        assertThatThrownBy(() -> service.book(req, "admin"))
                .isInstanceOf(BusinessException.class);
    }

    // ---- confirm / reject / noShow ----
    @Test
    void confirm_pendingToConfirmed() {
        Appointment a = appt(patient(user("pat")), futureSlot(doctor(user("doc")), true), AppointmentStatus.PENDING);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        service.confirm(a.getId());
        assertThat(a.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
    }

    @Test
    void confirm_throws_whenNotPending() {
        Appointment a = appt(patient(user("pat")), futureSlot(doctor(user("doc")), true), AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        assertThatThrownBy(() -> service.confirm(a.getId())).isInstanceOf(BusinessException.class);
    }

    @Test
    void reject_freesSlot() {
        Schedule s = futureSlot(doctor(user("doc")), true);
        Appointment a = appt(patient(user("pat")), s, AppointmentStatus.PENDING);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        service.reject(a.getId());
        assertThat(a.getStatus()).isEqualTo(AppointmentStatus.REJECTED);
        assertThat(s.isBooked()).isFalse();
    }

    @Test
    void noShow_confirmedToNoShow() {
        Appointment a = appt(patient(user("pat")), futureSlot(doctor(user("doc")), true), AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        service.noShow(a.getId());
        assertThat(a.getStatus()).isEqualTo(AppointmentStatus.NO_SHOW);
    }

    // ---- reschedule ----
    @Test
    void reschedule_movesToNewSlot() {
        Doctor d = doctor(user("doc"));
        Schedule oldS = futureSlot(d, true);
        Schedule newS = futureSlot(d, false);
        Appointment a = appt(patient(user("pat")), oldS, AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        when(scheduleRepository.findById(newS.getId())).thenReturn(Optional.of(newS));

        service.reschedule(a.getId(), newS.getId());

        assertThat(a.getStatus()).isEqualTo(AppointmentStatus.RESCHEDULED);
        assertThat(oldS.isBooked()).isFalse();
        assertThat(newS.isBooked()).isTrue();
        assertThat(a.getSchedule()).isSameAs(newS);
    }

    @Test
    void reschedule_throws_whenStatusInvalid() {
        Appointment a = appt(patient(user("pat")), futureSlot(doctor(user("doc")), true), AppointmentStatus.COMPLETED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        assertThatThrownBy(() -> service.reschedule(a.getId(), UUID.randomUUID()))
                .isInstanceOf(BusinessException.class);
    }

    // ---- complete ----
    @Test
    void complete_byDoctor_savesMedicalRecordEntry() {
        User docUser = user("doc");
        Doctor d = doctor(docUser);
        Appointment a = appt(patient(user("pat")), futureSlot(d, true), AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        when(userRepository.findByUsername("doc")).thenReturn(Optional.of(docUser));
        when(doctorRepository.findByUserId(docUser.getId())).thenReturn(Optional.of(d));
        CompleteAppointmentRequest req = new CompleteAppointmentRequest();
        req.setDiagnosis("dx");

        service.complete(a.getId(), req, "doc");

        assertThat(a.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
        verify(medicalRecordService).addEntry(any(), any());
    }

    @Test
    void complete_byNonDoctor_skipsMedicalRecord() {
        Appointment a = appt(patient(user("pat")), futureSlot(doctor(user("doc")), true), AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        service.complete(a.getId(), new CompleteAppointmentRequest(), "admin");

        assertThat(a.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
        verify(medicalRecordService, org.mockito.Mockito.never()).addEntry(any(), any());
    }

    // ---- mine ----
    @Test
    void mine_patientPath() {
        User u = user("pat");
        Patient p = patient(u);
        when(userRepository.findByUsername("pat")).thenReturn(Optional.of(u));
        when(patientRepository.findByUserId(u.getId())).thenReturn(Optional.of(p));
        when(appointmentRepository.findByPatientIdAndActiveTrue(p.getId()))
                .thenReturn(List.of(appt(p, futureSlot(doctor(user("doc")), true), AppointmentStatus.PENDING)));
        assertThat(service.mine("pat")).hasSize(1);
    }

    @Test
    void mine_doctorPath() {
        User u = user("doc");
        Doctor d = doctor(u);
        when(userRepository.findByUsername("doc")).thenReturn(Optional.of(u));
        when(patientRepository.findByUserId(u.getId())).thenReturn(Optional.empty());
        when(doctorRepository.findByUserId(u.getId())).thenReturn(Optional.of(d));
        when(appointmentRepository.findBySchedule_Doctor_IdAndActiveTrue(d.getId()))
                .thenReturn(List.of());
        assertThat(service.mine("doc")).isEmpty();
    }

    // ---- accept / cancel ----
    @Test
    void accept_ownerConfirms() {
        User pu = user("pat");
        Patient p = patient(pu);
        Appointment a = appt(p, futureSlot(doctor(user("doc")), true), AppointmentStatus.RESCHEDULED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        when(permissions.has("Appointment:Update")).thenReturn(false);

        service.accept(a.getId(), "pat");
        assertThat(a.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
    }

    @Test
    void cancel_adminBypassesOwnership() {
        Appointment a = appt(patient(user("pat")), futureSlot(doctor(user("doc")), true), AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        when(permissions.has("Appointment:Update")).thenReturn(true);

        service.cancel(a.getId(), "admin");
        assertThat(a.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    void cancel_throws_whenNotOwnerNorAdmin() {
        Appointment a = appt(patient(user("pat")), futureSlot(doctor(user("doc")), true), AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        when(permissions.has("Appointment:Update")).thenReturn(false);

        assertThatThrownBy(() -> service.cancel(a.getId(), "someoneElse"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void cancel_throws_whenTerminalState() {
        User pu = user("pat");
        Appointment a = appt(patient(pu), futureSlot(doctor(user("doc")), true), AppointmentStatus.COMPLETED);
        when(appointmentRepository.findById(a.getId())).thenReturn(Optional.of(a));
        when(permissions.has("Appointment:Update")).thenReturn(true);

        assertThatThrownBy(() -> service.cancel(a.getId(), "admin"))
                .isInstanceOf(BusinessException.class);
    }
}
