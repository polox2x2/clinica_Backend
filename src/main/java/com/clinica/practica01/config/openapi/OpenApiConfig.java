package com.clinica.practica01.config.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de OpenAPI/Swagger.
 * El esquema de seguridad es HTTP bearer: en el boton "Authorize" solo se pega
 * el TOKEN (sin escribir "Bearer "); springdoc agrega el prefijo automaticamente
 * y envia el header Authorization: Bearer &lt;token&gt;.
 * Swagger solo se habilita en local (ver application.yaml / application-prod.yaml).
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MediCitas API",
                version = "1.0",
                description = "API REST de gestion de citas medicas, historia clinica y farmacia. "
                        + "Autenticacion JWT (por cookie httpOnly en el frontend, o Bearer aqui en Swagger). "
                        + "Autorizacion por permisos granulares 'Entidad:Accion'."
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Pega solo el token JWT (sin 'Bearer '). Lo obtienes de POST /api/auth/login."
)
public class OpenApiConfig {
}
