package com.clinica.practica01.feature.user.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.feature.role.entity.Role;
import com.clinica.practica01.feature.role.repository.RoleRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountProvisionerTest {

    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UsernameGenerator usernameGenerator;

    AccountProvisioner provisioner;

    @BeforeEach
    void setUp() {
        provisioner = new AccountProvisioner(userRepository, roleRepository, passwordEncoder, usernameGenerator);
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        lenient().when(usernameGenerator.generate(anyString(), anyString())).thenReturn("a.b");
        lenient().when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void create_buildsAndSavesUser() {
        when(userRepository.existsByEmail("e@x.com")).thenReturn(false);
        when(roleRepository.findByName("Medico")).thenReturn(Optional.of(Role.builder().name("Medico").build()));

        User created = provisioner.create("A", "B", "e@x.com", "pw", "Medico");
        assertThat(created.getUsername()).isEqualTo("a.b");
        assertThat(created.getPassword()).isEqualTo("hashed");
        verify(userRepository).save(created);
    }

    @Test
    void create_throws_whenEmailInUse() {
        when(userRepository.existsByEmail("e@x.com")).thenReturn(true);
        assertThatThrownBy(() -> provisioner.create("A", "B", "e@x.com", "pw", "Medico"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void create_throws_whenRoleMissing() {
        when(userRepository.existsByEmail("e@x.com")).thenReturn(false);
        when(roleRepository.findByName("Nope")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> provisioner.create("A", "B", "e@x.com", "pw", "Nope"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateProfile_updatesAndSaves() {
        User u = User.builder().firstName("Old").build();
        provisioner.updateProfile(u, "New", "Name", "n@x.com");
        assertThat(u.getFirstName()).isEqualTo("New");
        assertThat(u.getEmail()).isEqualTo("n@x.com");
        verify(userRepository).save(u);
    }
}
