package com.clinica.practica01.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    private UserDetails user(String name) {
        return new User(name, "pw", List.of());
    }

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Secret >= 256 bits (HS256) y expiracion amplia por defecto.
        ReflectionTestUtils.setField(jwtService, "secret",
                "0123456789-0123456789-0123456789-0123456789");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3_600_000L);
    }

    @Test
    void generateAndExtractUsername_roundTrips() {
        String token = jwtService.generateToken(user("alice"));
        assertThat(jwtService.extractUsername(token)).isEqualTo("alice");
    }

    @Test
    void isValid_true_forFreshTokenAndSameUser() {
        String token = jwtService.generateToken(user("bob"));
        assertThat(jwtService.isValid(token, user("bob"))).isTrue();
    }

    @Test
    void isValid_false_forDifferentUser() {
        String token = jwtService.generateToken(user("bob"));
        assertThat(jwtService.isValid(token, user("carol"))).isFalse();
    }

    @Test
    void isValid_false_forExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "expirationMs", -1_000L);
        String token = jwtService.generateToken(user("dave"));
        assertThat(jwtService.isValid(token, user("dave"))).isFalse();
    }

    @Test
    void isValid_false_forGarbageToken() {
        assertThat(jwtService.isValid("not-a-jwt", user("eve"))).isFalse();
    }
}
