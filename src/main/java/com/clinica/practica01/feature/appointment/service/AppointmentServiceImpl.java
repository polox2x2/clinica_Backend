package com.clinica.practica01.feature.appointment.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.core.service.AbstractCrudService;
import com.clinica.practica01.feature.appointment.dto.AppointmentRequest;
import com.clinica.practica01.feature.appointment.dto.AppointmentResponse;
import com.clinica.practica01.feature.appointment.dto.CompleteAppointmentRequest;
import com.clinica.practica01.feature.appointment.entity.Appointment;
import com.clinica.practica01.feature.appointment.entity.AppointmentStatus;
import com.clinica.practica01.feature.appointment.mapper.AppointmentMapper;
import com.clinica.practica01.feature.appointment.notification.AppointmentNotification;
import com.clinica.practica01.feature.appointment.notification.AppointmentNotifier;
import com.clinica.practica01.feature.appointment.repository.AppointmentRepository;
import com.clinica.practica01.feature.calendar.notification.CalendarNotifier;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import com.clinica.practica01.feature.medicalrecord.dto.MedicalRecordEntryRequest;
import com.clinica.practica01.feature.medicalrecord.service.MedicalRecordService;
import com.clinica.practica01.feature.patient.entity.Patient;
import com.clinica.practica01.feature.patient.repository.PatientRepository;
import com.clinica.practica01.feature.schedule.entity.Schedule;
import com.clinica.practica01.feature.schedule.repository.ScheduleRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentServiceImpl
        extends AbstractCrudService<Appointment, AppointmentRequest, AppointmentResponse>
        implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentNotifier notifier;
    private final CalendarNotifier calendarNotifier;
    private final MedicalRecordService medicalRecordService;
    private final PermissionChecker permissions;

    public AppointmentServiceImpl(AppointmentRepository repository, AppointmentMapper mapper,
                                  ScheduleRepository scheduleRepository, PatientRepository patientRepository,
                                  UserRepository userRepository, DoctorRepository doctorRepository,
                                  AppointmentNotifier notifier, CalendarNotifier calendarNotifier,
                                  MedicalRecordService medicalRecordService, PermissionChecker permissions) {
        super(repository, mapper);
        this.appointmentRepository = repository;
        this.scheduleRepository = scheduleRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.notifier = notifier;
        this.calendarNotifier = calendarNotifier;
        this.medicalRecordService = medicalRecordService;
        this.permissions = permissions;
    }

    @Override
    protected String resourceName() {
        return "Appointment";
    }

    // ---- Reserva -----------------------------------------------------------

    @Override
    @Transactional
    public AppointmentResponse book(AppointmentRequest request, String username) {
        Patient patient = resolvePatient(request.getPatientId(), username);
        Schedule schedule = activeSchedule(request.getScheduleId());

        if (schedule.isBooked()) {
            throw new BusinessException("La franja ya esta reservada");
        }
        if (!isFuture(schedule)) {
            throw new BusinessException("No se puede reservar en el pasado", HttpStatus.BAD_REQUEST);
        }

        bookSlot(schedule, true);
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .schedule(schedule)
                .status(AppointmentStatus.PENDING)
                .notes(request.getNotes())
                .build();
        appointment.setActive(true);
        appointment = appointmentRepository.save(appointment);

        // Notifica al medico: nueva cita por confirmar
        notify(doctorUsername(schedule), appointment, "APPOINTMENT_PENDING",
                "Nueva cita por confirmar");
        return mapper.toResponseWithBase(appointment);
    }

    // ---- Transiciones del medico/admin ------------------------------------

    @Override
    @Transactional
    public AppointmentResponse confirm(UUID id) {
        Appointment a = getActiveOrThrow(id);
        requireStatus(a, AppointmentStatus.PENDING);
        a.setStatus(AppointmentStatus.CONFIRMED);
        a = appointmentRepository.save(a);
        notify(patientUsername(a), a, "APPOINTMENT_CONFIRMED", "Tu cita fue confirmada");
        return mapper.toResponseWithBase(a);
    }

    @Override
    @Transactional
    public AppointmentResponse reject(UUID id) {
        Appointment a = getActiveOrThrow(id);
        requireStatus(a, AppointmentStatus.PENDING);
        a.setStatus(AppointmentStatus.REJECTED);
        bookSlot(a.getSchedule(), false);
        a = appointmentRepository.save(a);
        notify(patientUsername(a), a, "APPOINTMENT_REJECTED", "Tu cita fue rechazada");
        return mapper.toResponseWithBase(a);
    }

    @Override
    @Transactional
    public AppointmentResponse reschedule(UUID id, UUID newScheduleId) {
        Appointment a = getActiveOrThrow(id);
        if (a.getStatus() != AppointmentStatus.PENDING && a.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new BusinessException("Solo se puede reprogramar una cita pendiente o confirmada");
        }
        Schedule newSchedule = activeSchedule(newScheduleId);
        if (newSchedule.isBooked()) {
            throw new BusinessException("La nueva franja ya esta reservada");
        }
        if (!isFuture(newSchedule)) {
            throw new BusinessException("La nueva franja no puede ser en el pasado", HttpStatus.BAD_REQUEST);
        }
        bookSlot(a.getSchedule(), false); // libera la anterior
        bookSlot(newSchedule, true);      // reserva la nueva
        a.setSchedule(newSchedule);
        a.setStatus(AppointmentStatus.RESCHEDULED);
        a = appointmentRepository.save(a);
        notify(patientUsername(a), a, "APPOINTMENT_RESCHEDULED",
                "El medico reprogramo tu cita: acepta o cancela");
        return mapper.toResponseWithBase(a);
    }

    @Override
    @Transactional
    public AppointmentResponse complete(UUID id, CompleteAppointmentRequest request, String username) {
        Appointment a = getActiveOrThrow(id);
        requireStatus(a, AppointmentStatus.CONFIRMED);
        a.setStatus(AppointmentStatus.COMPLETED);
        a = appointmentRepository.save(a);

        // Si quien completa es un medico, se guarda la atencion en la historia clinica.
        Optional<Doctor> doctor = userRepository.findByUsername(username)
                .flatMap(u -> doctorRepository.findByUserId(u.getId()));
        if (doctor.isPresent() && a.getPatient() != null) {
            MedicalRecordEntryRequest entry = new MedicalRecordEntryRequest();
            entry.setPatientId(a.getPatient().getId());
            entry.setAppointmentId(a.getId());
            entry.setReason(request.getReason());
            entry.setDiagnosis(request.getDiagnosis());
            entry.setTreatment(request.getTreatment());
            entry.setNotes(request.getObservations());
            medicalRecordService.addEntry(entry, username);
        }
        notify(patientUsername(a), a, "APPOINTMENT_COMPLETED", "Tu cita fue atendida");
        return mapper.toResponseWithBase(a);
    }

    @Override
    @Transactional
    public AppointmentResponse noShow(UUID id) {
        Appointment a = getActiveOrThrow(id);
        requireStatus(a, AppointmentStatus.CONFIRMED);
        a.setStatus(AppointmentStatus.NO_SHOW);
        a = appointmentRepository.save(a);
        notify(patientUsername(a), a, "APPOINTMENT_NO_SHOW", "Se registro tu inasistencia");
        return mapper.toResponseWithBase(a);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> mine(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        List<Appointment> appts = patientRepository.findByUserId(user.getId())
                .map(p -> appointmentRepository.findByPatientIdAndActiveTrue(p.getId()))
                .orElseGet(() -> doctorRepository.findByUserId(user.getId())
                        .map(d -> appointmentRepository.findBySchedule_Doctor_IdAndActiveTrue(d.getId()))
                        .orElseGet(List::of));
        return appts.stream().map(mapper::toResponseWithBase).toList();
    }

    // ---- Transiciones del paciente ----------------------------------------

    @Override
    @Transactional
    public AppointmentResponse accept(UUID id, String username) {
        Appointment a = getActiveOrThrow(id);
        requireOwnerOrAdmin(a, username);
        requireStatus(a, AppointmentStatus.RESCHEDULED);
        a.setStatus(AppointmentStatus.CONFIRMED);
        a = appointmentRepository.save(a);
        notify(doctorUsername(a.getSchedule()), a, "APPOINTMENT_CONFIRMED",
                "El paciente acepto la reprogramacion");
        return mapper.toResponseWithBase(a);
    }

    @Override
    @Transactional
    public AppointmentResponse cancel(UUID id, String username) {
        Appointment a = getActiveOrThrow(id);
        requireOwnerOrAdmin(a, username);
        if (a.getStatus() == AppointmentStatus.CANCELLED
                || a.getStatus() == AppointmentStatus.COMPLETED
                || a.getStatus() == AppointmentStatus.REJECTED) {
            throw new BusinessException("La cita no se puede cancelar en su estado actual");
        }
        a.setStatus(AppointmentStatus.CANCELLED);
        bookSlot(a.getSchedule(), false);
        a = appointmentRepository.save(a);
        notify(doctorUsername(a.getSchedule()), a, "APPOINTMENT_CANCELLED",
                "El paciente cancelo la cita");
        return mapper.toResponseWithBase(a);
    }

    // ---- Helpers -----------------------------------------------------------

    private Patient resolvePatient(UUID patientId, String username) {
        if (patientId != null) {
            return patientRepository.findById(patientId)
                    .filter(p -> p.isActive())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado: " + patientId));
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(
                        "No tienes un perfil de paciente; completalo antes de reservar", HttpStatus.BAD_REQUEST));
    }

    private Schedule activeSchedule(UUID scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .filter(s -> s.isActive())
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado: " + scheduleId));
    }

    private boolean isFuture(Schedule s) {
        return LocalDateTime.of(s.getAvailableDate(), s.getStartTime()).isAfter(LocalDateTime.now());
    }

    private void bookSlot(Schedule s, boolean booked) {
        s.setBooked(booked);
        scheduleRepository.save(s);
    }

    private void requireStatus(Appointment a, AppointmentStatus expected) {
        if (a.getStatus() != expected) {
            throw new BusinessException("Operacion invalida: la cita esta en estado " + a.getStatus());
        }
    }

    /** El paciente dueño de la cita, o un usuario con permiso de gestion (admin). */
    private void requireOwnerOrAdmin(Appointment a, String username) {
        if (permissions.has("Appointment:Update")) {
            return;
        }
        String owner = patientUsername(a);
        if (owner == null || !owner.equals(username)) {
            throw new BusinessException("No puedes operar sobre esta cita", HttpStatus.FORBIDDEN);
        }
    }

    private String patientUsername(Appointment a) {
        return a.getPatient() != null && a.getPatient().getUser() != null
                ? a.getPatient().getUser().getUsername() : null;
    }

    private String doctorUsername(Schedule s) {
        return s.getDoctor() != null && s.getDoctor().getUser() != null
                ? s.getDoctor().getUser().getUsername() : null;
    }

    private void notify(String username, Appointment a, String type, String message) {
        notifier.notifyUser(username,
                new AppointmentNotification(type, a.getId(), a.getStatus(), message));
        // Broadcast al calendario del medico para que las agendas abiertas refresquen
        if (a.getSchedule() != null && a.getSchedule().getDoctor() != null) {
            calendarNotifier.calendarChanged(a.getSchedule().getDoctor().getId(), type);
        }
    }

    // findByPatient / findByDoctor podrian exponerse luego; se dejan los finders listos.
    @SuppressWarnings("unused")
    private List<Appointment> byPatient(UUID patientId) {
        return appointmentRepository.findByPatientIdAndActiveTrue(patientId);
    }
}
