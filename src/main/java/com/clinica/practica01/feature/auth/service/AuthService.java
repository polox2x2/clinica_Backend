package com.clinica.practica01.feature.auth.service;

import com.clinica.practica01.feature.auth.dto.LoginRequest;
import com.clinica.practica01.feature.auth.dto.MeResponse;
import com.clinica.practica01.feature.auth.dto.RegisterRequest;

/** Contrato de autenticacion. */
public interface AuthService {

    AuthResult login(LoginRequest request);

    AuthResult register(RegisterRequest request);

    /** Datos de la sesion del usuario autenticado (por username). */
    MeResponse me(String username);
}
