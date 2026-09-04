package com.clinica.practica01.feature.patient.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.core.service.AbstractCrudService;
import com.clinica.practica01.feature.patient.dto.PatientRequest;
import com.clinica.practica01.feature.patient.dto.PatientResponse;
import com.clinica.practica01.feature.patient.dto.SelfPatientRequest;
import com.clinica.practica01.feature.patient.entity.Patient;
import com.clinica.practica01.feature.patient.mapper.PatientMapper;
import com.clinica.practica01.feature.patient.repository.PatientRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import com.clinica.practica01.feature.user.service.AccountProvisioner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl
        extends AbstractCrudService<Patient, PatientRequest, PatientResponse>
        implements PatientService {

    private static final String PATIENT_ROLE = "Paciente";

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AccountProvisioner accountProvisioner;

    public PatientServiceImpl(PatientRepository repository, PatientMapper mapper,
                              UserRepository userRepository, AccountProvisioner accountProvisioner) {
        super(repository, mapper);
        this.patientRepository = repository;
        this.userRepository = userRepository;
        this.accountProvisioner = accountProvisioner;
    }

    @Override
    protected String resourceName() {
        return "Patient";
    }

    @Override
    protected List<String> searchableFields() {
        return List.of("documentId", "user.firstName", "user.lastName");
    }

    // ---- Alta por admin (crea tambien la cuenta) --------------------------

    @Override
    @Transactional
    public PatientResponse create(PatientRequest request) {
        User user = accountProvisioner.create(
                request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), PATIENT_ROLE);

        Patient patient = mapper.toEntity(request);
        patient.setUser(user);
        patient.setActive(true);
        return mapper.toResponseWithBase(patientRepository.save(patient));
    }

    @Override
    @Transactional
    public PatientResponse update(UUID id, PatientRequest request) {
        Patient patient = getActiveOrThrow(id);
        mapper.updateEntity(patient, request);
        if (patient.getUser() != null) {
            accountProvisioner.updateProfile(patient.getUser(),
                    request.getFirstName(), request.getLastName(), request.getEmail());
        }
        return mapper.toResponseWithBase(patientRepository.save(patient));
    }

    // ---- Self-service del propio paciente ---------------------------------

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getMe(String username) {
        return mapper.toResponseWithBase(myPatient(username));
    }

    @Override
    @Transactional
    public PatientResponse createMe(String username, SelfPatientRequest request) {
        User user = currentUser(username);
        if (patientRepository.findByUserId(user.getId()).isPresent()) {
            throw new BusinessException("Ya tienes un perfil de paciente");
        }
        Patient patient = Patient.builder()
                .documentId(request.getDocumentId())
                .dateOfBirth(request.getDateOfBirth())
                .phone(request.getPhone())
                .user(user)
                .build();
        patient.setActive(true);
        return mapper.toResponseWithBase(patientRepository.save(patient));
    }

    @Override
    @Transactional
    public PatientResponse updateMe(String username, SelfPatientRequest request) {
        Patient patient = myPatient(username);
        patient.setDocumentId(request.getDocumentId());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setPhone(request.getPhone());
        return mapper.toResponseWithBase(patientRepository.save(patient));
    }

    private User currentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private Patient myPatient(String username) {
        User user = currentUser(username);
        return patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(
                        "Aun no tienes un perfil de paciente", HttpStatus.NOT_FOUND));
    }
}
