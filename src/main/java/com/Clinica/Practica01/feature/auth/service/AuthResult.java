package com.Clinica.Practica01.feature.auth.service;

import com.Clinica.Practica01.feature.auth.dto.MeResponse;

/** Resultado interno de autenticacion: el token (para la cookie) + datos de sesion. */
public record AuthResult(String token, MeResponse me) {
}
