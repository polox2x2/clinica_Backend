package com.clinica.practica01.feature.doctor.service;

import com.clinica.practica01.core.service.AbstractCrudService;
import com.clinica.practica01.feature.doctor.dto.DoctorRequest;
import com.clinica.practica01.feature.doctor.dto.DoctorResponse;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import com.clinica.practica01.feature.doctor.mapper.DoctorMapper;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import com.clinica.practica01.feature.user.service.AccountProvisioner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl
        extends AbstractCrudService<Doctor, DoctorRequest, DoctorResponse>
        implements DoctorService {

    private static final String DOCTOR_ROLE = "Medico";

    private final AccountProvisioner accountProvisioner;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository repository, DoctorMapper mapper,
                             AccountProvisioner accountProvisioner, UserRepository userRepository) {
        super(repository, mapper);
        this.doctorRepository = repository;
        this.accountProvisioner = accountProvisioner;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getMe(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.clinica.practica01.core.exception.ResourceNotFoundException(
                        "Usuario no encontrado"));
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .filter(Doctor::isActive)
                .orElseThrow(() -> new com.clinica.practica01.core.exception.ResourceNotFoundException(
                        "No tienes perfil de medico"));
        return mapper.toResponseWithBase(doctor);
    }

    @Override
    protected String resourceName() {
        return "Doctor";
    }

    @Override
    protected List<String> searchableFields() {
        return List.of("cmp", "user.firstName", "user.lastName");
    }

    /** Crea la cuenta (User rol Medico) y el perfil de medico en un solo paso. */
    @Override
    @Transactional
    public DoctorResponse create(DoctorRequest request) {
        User user = accountProvisioner.create(
                request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), DOCTOR_ROLE);

        Doctor doctor = mapper.toEntity(request); // cmp + especialidad
        doctor.setUser(user);
        doctor.setActive(true);
        return mapper.toResponseWithBase(repository.save(doctor));
    }

    @Override
    @Transactional
    public DoctorResponse update(UUID id, DoctorRequest request) {
        Doctor doctor = getActiveOrThrow(id);
        mapper.updateEntity(doctor, request); // cmp + especialidad
        if (doctor.getUser() != null) {
            accountProvisioner.updateProfile(doctor.getUser(),
                    request.getFirstName(), request.getLastName(), request.getEmail());
        }
        return mapper.toResponseWithBase(repository.save(doctor));
    }
}
