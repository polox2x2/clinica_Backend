package com.Clinica.Practica01.feature.auth.controller;

import com.Clinica.Practica01.core.security.CookieService;
import com.Clinica.Practica01.feature.auth.dto.LoginRequest;
import com.Clinica.Practica01.feature.auth.dto.MeResponse;
import com.Clinica.Practica01.feature.auth.dto.RegisterRequest;
import com.Clinica.Practica01.feature.auth.service.AuthResult;
import com.Clinica.Practica01.feature.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Autenticacion por cookie httpOnly: login/register colocan el JWT en una
 * cookie (no viaja al JS). El frontend solo llama /me al montar para restaurar
 * la sesion tras un F5 (la cookie viaja sola).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Login, registro y sesion (cookie JWT httpOnly)")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @Operation(summary = "Iniciar sesion",
            description = "Valida credenciales, deja la cookie httpOnly con el JWT y devuelve la sesion "
                    + "(usuario, roles, permisos). Publico.")
    @PostMapping("/login")
    public ResponseEntity<MeResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = authService.login(request);
        return withAuthCookie(result, HttpStatus.OK);
    }

    @Operation(summary = "Registro publico",
            description = "Crea una cuenta con rol por defecto (Paciente), username autogenerado, "
                    + "deja la cookie y devuelve la sesion. Publico.")
    @PostMapping("/register")
    public ResponseEntity<MeResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResult result = authService.register(request);
        return withAuthCookie(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Cerrar sesion", description = "Limpia la cookie del JWT. Publico.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cleared = cookieService.clear();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cleared.toString())
                .build();
    }

    @Operation(summary = "Sesion actual",
            description = "Devuelve el usuario autenticado con sus roles y permisos. "
                    + "El frontend lo llama al montar (F5) para restaurar la sesion.")
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        return ResponseEntity.ok(authService.me(authentication.getName()));
    }

    private ResponseEntity<MeResponse> withAuthCookie(AuthResult result, HttpStatus status) {
        ResponseCookie cookie = cookieService.build(result.token());
        return ResponseEntity.status(status)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.me());
    }
}
