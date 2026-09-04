package com.clinica.practica01;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Integracion del CORE con auth por cookie: login (admin sembrado) deja la
 * cookie httpOnly; con esa cookie /me devuelve permisos; sin cookie da 401/403.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AuthIT {

    @Container
    @SuppressWarnings("resource")
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    }

    @LocalServerPort
    int port;

    private final HttpClient client = HttpClient.newHttpClient();

    private String base() {
        return "http://localhost:" + port;
    }

    @Test
    void loginDejaCookieYMeDevuelvePermisos() throws Exception {
        // 1. Login: 200 + Set-Cookie con el token, y el body ya trae los datos de sesion
        HttpResponse<String> login = client.send(HttpRequest.newBuilder()
                .uri(URI.create(base() + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .build(), HttpResponse.BodyHandlers.ofString());

        assertThat(login.statusCode()).isEqualTo(200);
        assertThat(login.body()).contains("\"username\":\"admin\"");
        assertThat(login.body()).contains("User:List");

        Optional<String> setCookie = login.headers().firstValue("Set-Cookie");
        assertThat(setCookie).isPresent();
        assertThat(setCookie.get()).contains("access_token=");
        assertThat(setCookie.get()).contains("HttpOnly");

        String cookie = setCookie.get().split(";")[0]; // access_token=...

        // 2. /me con la cookie -> 200 + permisos + menu (simula el F5 del front)
        HttpResponse<String> me = client.send(HttpRequest.newBuilder()
                .uri(URI.create(base() + "/api/auth/me"))
                .header("Cookie", cookie)
                .GET()
                .build(), HttpResponse.BodyHandlers.ofString());

        assertThat(me.statusCode()).isEqualTo(200);
        assertThat(me.body()).contains("\"username\":\"admin\"");

        // 2b. El arbol de menu del admin trae los items sembrados
        HttpResponse<String> tree = client.send(HttpRequest.newBuilder()
                .uri(URI.create(base() + "/api/menus/tree"))
                .header("Cookie", cookie)
                .GET()
                .build(), HttpResponse.BodyHandlers.ofString());
        assertThat(tree.statusCode()).isEqualTo(200);
        assertThat(tree.body()).contains("Seguridad");
        assertThat(tree.body()).contains("Usuarios");
        assertThat(tree.body()).contains("\"children\"");

        // 3. Sin cookie -> endpoint protegido responde 401/403
        HttpResponse<String> noAuth = client.send(HttpRequest.newBuilder()
                .uri(URI.create(base() + "/api/users"))
                .GET()
                .build(), HttpResponse.BodyHandlers.ofString());
        assertThat(noAuth.statusCode()).isIn(401, 403);
    }
}
