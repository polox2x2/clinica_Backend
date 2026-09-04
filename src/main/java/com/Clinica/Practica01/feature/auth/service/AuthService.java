package com.Clinica.Practica01.feature.auth.service;

import com.Clinica.Practica01.feature.auth.dto.LoginRequest;
import com.Clinica.Practica01.feature.auth.dto.MeResponse;
import com.Clinica.Practica01.feature.auth.dto.RegisterRequest;

/** Contrato de autenticacion. */
public interface AuthService {

    AuthResult login(LoginRequest request);

    AuthResult register(RegisterRequest request);

    /** Datos de la sesion del usuario autenticado (por username). */
    MeResponse me(String username);
}
