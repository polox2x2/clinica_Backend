package com.Clinica.Practica01;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Prueba de integracion end-to-end: arranca la app en un puerto real con una
 * BD MySQL efimera (Testcontainers) y verifica el endpoint de Actuator.
 * La corre failsafe bajo el perfil 'integration' (convencion *IT).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class HealthCheckIT {

	@Container
	@SuppressWarnings("resource")
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

	@DynamicPropertySource
	static void datasourceProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
		registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
	}

	@LocalServerPort
	private int port;

	@Test
	void healthDevuelveUp() throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest req = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:" + port + "/actuator/health"))
			.GET()
			.build();
		HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
		assertThat(res.statusCode()).isEqualTo(200);
		assertThat(res.body()).contains("\"status\":\"UP\"");
	}

}
