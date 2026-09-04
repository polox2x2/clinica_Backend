package com.clinica.practica01.feature.auth.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.core.security.JwtService;
import com.clinica.practica01.feature.auth.dto.LoginRequest;
import com.clinica.practica01.feature.auth.dto.MeResponse;
import com.clinica.practica01.feature.auth.dto.RegisterRequest;
import com.clinica.practica01.feature.role.entity.Role;
import com.clinica.practica01.feature.role.repository.RoleRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import com.clinica.practica01.feature.user.service.UsernameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsernameGenerator usernameGenerator;

    @Value("${app.auth.default-role:Paciente}")
    private String defaultRole;

    @Override
    public AuthResult login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BusinessException("Credenciales invalidas", HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return build(user);
    }

    @Override
    public AuthResult register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya esta en uso");
        }
        Set<Role> roles = new HashSet<>();
        roleRepository.findByName(defaultRole).ifPresent(roles::add);

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(usernameGenerator.generate(request.getFirstName(), request.getLastName()))
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        return build(userRepository.save(user));
    }

    @Override
    public MeResponse me(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return toMe(user);
    }

    private AuthResult build(User user) {
        return new AuthResult(jwtService.generateToken(user), toMe(user));
    }

    private MeResponse toMe(User user) {
        Set<String> permissions = user.permissionNames();
        return MeResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.roleNames())
                .permissions(permissions)
                .build();
    }
}
