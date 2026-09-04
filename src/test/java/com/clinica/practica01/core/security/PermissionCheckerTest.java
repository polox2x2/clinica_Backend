package com.clinica.practica01.core.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class PermissionCheckerTest {

    private final PermissionChecker checker = new PermissionChecker();

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateWith(String... authorities) {
        var auths = List.of(authorities).stream().map(SimpleGrantedAuthority::new).toList();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user", "pw", auths));
    }

    @Test
    void has_returnsTrue_whenAuthorityPresent() {
        authenticateWith("User:Read", "User:List");
        assertThat(checker.has("User:Read")).isTrue();
    }

    @Test
    void has_returnsFalse_whenAuthorityMissing() {
        authenticateWith("User:Read");
        assertThat(checker.has("User:Delete")).isFalse();
    }

    @Test
    void has_returnsFalse_whenNotAuthenticated() {
        SecurityContextHolder.clearContext();
        assertThat(checker.has("User:Read")).isFalse();
    }

    @Test
    void require_throws_whenPermissionMissing() {
        authenticateWith("User:Read");
        assertThatThrownBy(() -> checker.require("User:Delete"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("User:Delete");
    }

    @Test
    void require_doesNotThrow_whenPermissionPresent() {
        authenticateWith("User:Delete");
        assertThatCode(() -> checker.require("User:Delete")).doesNotThrowAnyException();
    }
}
