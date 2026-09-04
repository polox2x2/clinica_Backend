package com.clinica.practica01.feature.medicalrecord.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.feature.appointment.repository.AppointmentRepository;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import com.clinica.practica01.feature.medicalrecord.dto.MedicalRecordEntryRequest;
import com.clinica.practica01.feature.medicalrecord.entity.MedicalRecord;
import com.clinica.practica01.feature.medicalrecord.entity.MedicalRecordEntry;
import com.clinica.practica01.feature.medicalrecord.repository.MedicalRecordEntryRepository;
import com.clinica.practica01.feature.medicalrecord.repository.MedicalRecordRepository;
import com.clinica.practica01.feature.patient.entity.Patient;
import com.clinica.practica01.feature.patient.repository.PatientRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MedicalRecordServiceImplTest {

    @Mock MedicalRecordRepository recordRepository;
    @Mock MedicalRecordEntryRepository entryRepository;
    @Mock PatientRepository patientRepository;
    @Mock DoctorRepository doctorRepository;
    @Mock UserRepository userRepository;
    @Mock AppointmentRepository appointmentRepository;

    MedicalRecordServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MedicalRecordServiceImpl(recordRepository, entryRepository, patientRepository,
                doctorRepository, userRepository, appointmentRepository);
        lenient().when(entryRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        lenient().when(recordRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    private User user(String n) { User u = new User(); u.setId(UUID.randomUUID()); u.setUsername(n); u.setFirstName("F"); u.setLastName("L"); return u; }
    private Doctor doctor(User u) { Doctor d = Doctor.builder().user(u).build(); d.setId(UUID.randomUUID()); return d; }
    private Patient patient(User u) { Patient p = Patient.builder().user(u).build(); p.setId(UUID.randomUUID()); return p; }

    @Test
    void addEntry_createsRecordIfMissing_andSavesEntry() {
        User du = user("doc");
        Doctor d = doctor(du);
        Patient p = patient(user("pat"));
        MedicalRecordEntryRequest req = new MedicalRecordEntryRequest();
        req.setPatientId(p.getId());
        req.setReason("r");
        when(userRepository.findByUsername("doc")).thenReturn(Optional.of(du));
        when(doctorRepository.findByUserId(du.getId())).thenReturn(Optional.of(d));
        when(patientRepository.findById(p.getId())).thenReturn(Optional.of(p));
        when(recordRepository.findByPatientId(p.getId())).thenReturn(Optional.empty());

        assertThat(service.addEntry(req, "doc")).isNotNull();
        verify(entryRepository).save(any(MedicalRecordEntry.class));
    }

    @Test
    void addEntry_throws_whenUserNotDoctor() {
        User du = user("nodoc");
        Patient p = patient(user("pat"));
        MedicalRecordEntryRequest req = new MedicalRecordEntryRequest();
        req.setPatientId(p.getId());
        when(userRepository.findByUsername("nodoc")).thenReturn(Optional.of(du));
        when(doctorRepository.findByUserId(du.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addEntry(req, "nodoc"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void getByPatient_returnsResponseWithEntries() {
        Patient p = patient(user("pat"));
        MedicalRecord rec = MedicalRecord.builder().patient(p).allergies("none").bloodType("O+").build();
        rec.setId(UUID.randomUUID());
        when(recordRepository.findByPatientId(p.getId())).thenReturn(Optional.of(rec));
        when(entryRepository.findByMedicalRecordIdAndActiveTrueOrderByCreatedAtDesc(rec.getId()))
                .thenReturn(List.of());

        var res = service.getByPatient(p.getId());
        assertThat(res.getBloodType()).isEqualTo("O+");
        assertThat(res.getPatientId()).isEqualTo(p.getId());
    }

    @Test
    void getByPatient_throws_whenNoRecord() {
        UUID pid = UUID.randomUUID();
        when(recordRepository.findByPatientId(pid)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getByPatient(pid))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getMine_delegatesToPatientRecord() {
        User u = user("pat");
        Patient p = patient(u);
        MedicalRecord rec = MedicalRecord.builder().patient(p).build();
        rec.setId(UUID.randomUUID());
        when(userRepository.findByUsername("pat")).thenReturn(Optional.of(u));
        when(patientRepository.findByUserId(u.getId())).thenReturn(Optional.of(p));
        when(recordRepository.findByPatientId(p.getId())).thenReturn(Optional.of(rec));
        when(entryRepository.findByMedicalRecordIdAndActiveTrueOrderByCreatedAtDesc(rec.getId()))
                .thenReturn(List.of());

        assertThat(service.getMine("pat")).isNotNull();
    }

    @Test
    void getMine_throws_whenNoPatientProfile() {
        User u = user("pat");
        when(userRepository.findByUsername("pat")).thenReturn(Optional.of(u));
        when(patientRepository.findByUserId(u.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getMine("pat"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
