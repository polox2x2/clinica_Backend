package com.Clinica.Practica01.feature.user.mapper;

import com.Clinica.Practica01.core.mapper.BaseMapper;
import com.Clinica.Practica01.feature.role.entity.Role;
import com.Clinica.Practica01.feature.role.repository.RoleRepository;
import com.Clinica.Practica01.feature.user.dto.UserRequest;
import com.Clinica.Practica01.feature.user.dto.UserResponse;
import com.Clinica.Practica01.feature.user.entity.User;
import com.Clinica.Practica01.feature.user.service.UsernameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper implements BaseMapper<User, UserRequest, UserResponse> {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsernameGenerator usernameGenerator;

    @Override
    public User toEntity(UserRequest r) {
        User user = User.builder()
                .firstName(r.getFirstName())
                .lastName(r.getLastName())
                .username(usernameGenerator.generate(r.getFirstName(), r.getLastName()))
                .email(r.getEmail())
                .password(passwordEncoder.encode(r.getPassword() == null ? "" : r.getPassword()))
                .build();
        user.setRoles(resolve(r.getRoleIds()));
        return user;
    }

    @Override
    public void updateEntity(User e, UserRequest r) {
        // El username NO se regenera al actualizar (se mantiene estable).
        e.setFirstName(r.getFirstName());
        e.setLastName(r.getLastName());
        e.setEmail(r.getEmail());
        if (r.getPassword() != null && !r.getPassword().isBlank()) {
            e.setPassword(passwordEncoder.encode(r.getPassword()));
        }
        e.setRoles(resolve(r.getRoleIds()));
    }

    @Override
    public UserResponse toResponse(User e) {
        UserResponse res = new UserResponse();
        res.setFirstName(e.getFirstName());
        res.setLastName(e.getLastName());
        res.setUsername(e.getUsername());
        res.setEmail(e.getEmail());
        res.setRoles(e.roleNames());
        return res;
    }

    private Set<Role> resolve(Set<UUID> ids) {
        Set<Role> set = new HashSet<>();
        if (ids != null) {
            ids.forEach(id -> roleRepository.findById(id).ifPresent(set::add));
        }
        return set;
    }
}
