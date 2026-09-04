package com.clinica.practica01.feature.auth.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.core.security.JwtService;
import com.clinica.practica01.feature.auth.dto.LoginRequest;
import com.clinica.practica01.feature.auth.dto.RegisterRequest;
import com.clinica.practica01.feature.role.entity.Role;
import com.clinica.practica01.feature.role.repository.RoleRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import com.clinica.practica01.feature.user.service.UsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceImplTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock JwtService jwtService;
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UsernameGenerator usernameGenerator;

    AuthServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AuthServiceImpl(authenticationManager, jwtService, userRepository,
                roleRepository, passwordEncoder, usernameGenerator);
        ReflectionTestUtils.setField(service, "defaultRole", "Paciente");
        lenient().when(jwtService.generateToken(any())).thenReturn("jwt-token");
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        lenient().when(usernameGenerator.generate(anyString(), anyString())).thenReturn("a.b");
        lenient().when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    private User user(String n) { User u = new User(); u.setId(UUID.randomUUID()); u.setUsername(n); return u; }

    @Test
    void login_returnsTokenAndMe() {
        LoginRequest req = new LoginRequest();
        req.setUsername("alice");
        req.setPassword("pw");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user("alice")));

        AuthResult result = service.login(req);
        assertThat(result.token()).isEqualTo("jwt-token");
        assertThat(result.me().getUsername()).isEqualTo("alice");
    }

    @Test
    void login_throws_onBadCredentials() {
        LoginRequest req = new LoginRequest();
        req.setUsername("alice");
        req.setPassword("bad");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("nope"));

        assertThatThrownBy(() -> service.login(req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void register_createsUserWithDefaultRole() {
        RegisterRequest req = new RegisterRequest();
        req.setFirstName("A");
        req.setLastName("B");
        req.setEmail("e@x.com");
        req.setPassword("pw");
        when(userRepository.existsByEmail("e@x.com")).thenReturn(false);
        when(roleRepository.findByName("Paciente")).thenReturn(Optional.of(Role.builder().name("Paciente").build()));

        AuthResult result = service.register(req);
        assertThat(result.token()).isEqualTo("jwt-token");
    }

    @Test
    void register_throws_whenEmailInUse() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("e@x.com");
        when(userRepository.existsByEmail("e@x.com")).thenReturn(true);
        assertThatThrownBy(() -> service.register(req)).isInstanceOf(BusinessException.class);
    }

    @Test
    void me_returnsProfile() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user("alice")));
        assertThat(service.me("alice").getUsername()).isEqualTo("alice");
    }

    @Test
    void me_throws_whenMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.me("ghost")).isInstanceOf(ResourceNotFoundException.class);
    }
}
