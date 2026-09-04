package com.clinica.practica01.config.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/** Convierte el username puesto por el interceptor en el Principal de la sesion WS. */
@Component
public class UsernameHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        Object username = attributes.get("username");
        if (username == null) {
            return null;
        }
        String name = username.toString();
        return () -> name;
    }
}
