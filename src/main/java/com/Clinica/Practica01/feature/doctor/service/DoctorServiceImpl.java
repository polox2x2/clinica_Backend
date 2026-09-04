package com.Clinica.Practica01.feature.doctor.service;

import com.Clinica.Practica01.core.service.AbstractCrudService;
import com.Clinica.Practica01.feature.doctor.dto.DoctorRequest;
import com.Clinica.Practica01.feature.doctor.dto.DoctorResponse;
import com.Clinica.Practica01.feature.doctor.entity.Doctor;
import com.Clinica.Practica01.feature.doctor.mapper.DoctorMapper;
import com.Clinica.Practica01.feature.doctor.repository.DoctorRepository;
import com.Clinica.Practica01.feature.user.entity.User;
import com.Clinica.Practica01.feature.user.service.AccountProvisioner;
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

    public DoctorServiceImpl(DoctorRepository repository, DoctorMapper mapper,
                             AccountProvisioner accountProvisioner) {
        super(repository, mapper);
        this.accountProvisioner = accountProvisioner;
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
