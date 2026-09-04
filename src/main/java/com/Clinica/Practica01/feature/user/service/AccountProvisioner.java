package com.Clinica.Practica01.feature.user.service;

import com.Clinica.Practica01.core.exception.BusinessException;
import com.Clinica.Practica01.core.exception.ResourceNotFoundException;
import com.Clinica.Practica01.feature.role.entity.Role;
import com.Clinica.Practica01.feature.role.repository.RoleRepository;
import com.Clinica.Practica01.feature.user.entity.User;
import com.Clinica.Practica01.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Crea cuentas de usuario con un rol asignado (username autogenerado, password
 * encriptado). Lo usan los modulos que crean su propia cuenta (medico, paciente).
 */
@Service
@RequiredArgsConstructor
public class AccountProvisioner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsernameGenerator usernameGenerator;

    public User create(String firstName, String lastName, String email, String password, String roleName) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("El email ya esta en uso");
        }
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(usernameGenerator.generate(firstName, lastName))
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();
        return userRepository.save(user);
    }

    /** Actualiza nombre/email de la cuenta enlazada. */
    public void updateProfile(User user, String firstName, String lastName, String email) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userRepository.save(user);
    }
}
