package com.clinica.practica01.feature.user.service;

import com.clinica.practica01.feature.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameGeneratorTest {

    @Mock UserRepository userRepository;
    @InjectMocks UsernameGenerator generator;

    @Test
    void generate_buildsFromNames_stripsAccents() {
        when(userRepository.existsByUsername("luis.martinezq")).thenReturn(false);
        assertThat(generator.generate("Luis Enrique", "Martinez Quijandria"))
                .isEqualTo("luis.martinezq");
    }

    @Test
    void generate_singleSurname_noTrailingDot() {
        when(userRepository.existsByUsername("ana.gomez")).thenReturn(false);
        assertThat(generator.generate("Ana", "Gomez")).isEqualTo("ana.gomez");
    }

    @Test
    void generate_appendsNumber_onCollision() {
        when(userRepository.existsByUsername("ana.gomez")).thenReturn(true);
        when(userRepository.existsByUsername("ana.gomez2")).thenReturn(false);
        assertThat(generator.generate("Ana", "Gomez")).isEqualTo("ana.gomez2");
    }

    @Test
    void generate_blankNames_fallsBackToUser() {
        when(userRepository.existsByUsername("user")).thenReturn(false);
        assertThat(generator.generate("", "")).isEqualTo("user");
    }
}
