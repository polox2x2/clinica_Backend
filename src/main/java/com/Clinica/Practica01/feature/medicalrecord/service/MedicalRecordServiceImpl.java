package com.Clinica.Practica01.feature.medicalrecord.service;

import com.Clinica.Practica01.core.exception.BusinessException;
import com.Clinica.Practica01.core.exception.ResourceNotFoundException;
import com.Clinica.Practica01.feature.appointment.repository.AppointmentRepository;
import com.Clinica.Practica01.feature.doctor.entity.Doctor;
import com.Clinica.Practica01.feature.doctor.repository.DoctorRepository;
import com.Clinica.Practica01.feature.medicalrecord.dto.MedicalRecordEntryRequest;
import com.Clinica.Practica01.feature.medicalrecord.dto.MedicalRecordEntryResponse;
import com.Clinica.Practica01.feature.medicalrecord.dto.MedicalRecordResponse;
import com.Clinica.Practica01.feature.medicalrecord.entity.MedicalRecord;
import com.Clinica.Practica01.feature.medicalrecord.entity.MedicalRecordEntry;
import com.Clinica.Practica01.feature.medicalrecord.repository.MedicalRecordEntryRepository;
import com.Clinica.Practica01.feature.medicalrecord.repository.MedicalRecordRepository;
import com.Clinica.Practica01.feature.patient.entity.Patient;
import com.Clinica.Practica01.feature.patient.repository.PatientRepository;
import com.Clinica.Practica01.feature.user.entity.User;
import com.Clinica.Practica01.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository recordRepository;
    private final MedicalRecordEntryRepository entryRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public MedicalRecordEntryResponse addEntry(MedicalRecordEntryRequest request, String username) {
        Doctor doctor = currentDoctor(username);
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        MedicalRecord record = recordRepository.findByPatientId(patient.getId())
                .orElseGet(() -> recordRepository.save(
                        MedicalRecord.builder().patient(patient).build()));

        MedicalRecordEntry entry = MedicalRecordEntry.builder()
                .record(record)
                .doctor(doctor)
                .reason(request.getReason())
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .notes(request.getNotes())
                .build();
        if (request.getAppointmentId() != null) {
            appointmentRepository.findById(request.getAppointmentId()).ifPresent(entry::setAppointment);
        }
        return toEntryResponse(entryRepository.save(entry));
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordResponse getByPatient(UUID patientId) {
        MedicalRecord record = recordRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El paciente no tiene historia clinica todavia"));
        List<MedicalRecordEntryResponse> entries =
                entryRepository.findByRecordIdAndActiveTrueOrderByCreatedAtDesc(record.getId())
                        .stream().map(this::toEntryResponse).toList();

        String patientName = record.getPatient().getUser() != null
                ? record.getPatient().getUser().getFirstName() + " "
                        + record.getPatient().getUser().getLastName()
                : null;

        return MedicalRecordResponse.builder()
                .id(record.getId())
                .patientId(record.getPatient().getId())
                .patientName(patientName)
                .allergies(record.getAllergies())
                .bloodType(record.getBloodType())
                .entries(entries)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordResponse getMine(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        var patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No tienes perfil de paciente"));
        return getByPatient(patient.getId());
    }

    private Doctor currentDoctor(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(
                        "Solo un medico puede registrar en la historia clinica", HttpStatus.FORBIDDEN));
    }

    private MedicalRecordEntryResponse toEntryResponse(MedicalRecordEntry e) {
        MedicalRecordEntryResponse res = new MedicalRecordEntryResponse();
        res.setId(e.getId());
        res.setActive(e.isActive());
        res.setCreatedAt(e.getCreatedAt());
        res.setReason(e.getReason());
        res.setDiagnosis(e.getDiagnosis());
        res.setTreatment(e.getTreatment());
        res.setNotes(e.getNotes());
        if (e.getDoctor() != null) {
            res.setDoctorId(e.getDoctor().getId());
            if (e.getDoctor().getUser() != null) {
                res.setDoctorName(e.getDoctor().getUser().getFirstName() + " "
                        + e.getDoctor().getUser().getLastName());
            }
        }
        if (e.getAppointment() != null) {
            res.setAppointmentId(e.getAppointment().getId());
        }
        return res;
    }
}
