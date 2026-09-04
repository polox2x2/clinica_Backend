package com.Clinica.Practica01.feature.user.service;

import com.Clinica.Practica01.core.service.AbstractCrudService;
import com.Clinica.Practica01.feature.user.dto.UserRequest;
import com.Clinica.Practica01.feature.user.dto.UserResponse;
import com.Clinica.Practica01.feature.user.entity.User;
import com.Clinica.Practica01.feature.user.mapper.UserMapper;
import com.Clinica.Practica01.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl
        extends AbstractCrudService<User, UserRequest, UserResponse>
        implements UserService {

    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected String resourceName() {
        return "User";
    }

    @Override
    protected List<String> searchableFields() {
        return List.of("username", "email");
    }
}
