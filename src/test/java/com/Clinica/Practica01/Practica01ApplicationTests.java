package com.Clinica.Practica01;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Prueba unitaria de arranque del contexto.
 * Usa Testcontainers (MySQL efimero) para satisfacer el datasource sin depender
 * de una BD externa. La corre surefire (convencion *Tests).
 */
@SpringBootTest
@Testcontainers
class Practica01ApplicationTests {

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

	@Test
	void contextLoads() {
	}

}
