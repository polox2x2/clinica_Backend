package com.clinica.practica01.config;

import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Cadena de seguridad de MAXIMA prioridad acotada SOLO a los endpoints de Actuator.
 * Los deja publicos para que los healthchecks (Docker/compose/pipeline) funcionen,
 * sin interferir con la seguridad del resto de la aplicacion.
 */
@Configuration
public class ActuatorSecurityConfig {

	@Bean
	@Order(1)
	SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher(EndpointRequest.toAnyEndpoint())
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
			.csrf(csrf -> csrf.disable());
		return http.build();
	}
}
