package com.clinica.practica01.config.websocket;

import com.clinica.practica01.core.security.CookieService;
import com.clinica.practica01.core.security.JwtService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Autentica el handshake del WebSocket leyendo el JWT de la cookie httpOnly
 * (viaja en el request de upgrade) y deja el username en los atributos de la
 * sesion, para enrutar mensajes por usuario.
 */
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;
    private final CookieService cookieService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            Cookie[] cookies = servletRequest.getServletRequest().getCookies();
            String username = extractUsernameFromCookies(cookies);
            if (username != null) {
                attributes.put("username", username);
            }
        }
        return true; // se permite el handshake; sin username no recibe mensajes de usuario
    }

    /** Busca la cookie del JWT y devuelve el username, o null si no hay token valido. */
    private String extractUsernameFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieService.getCookieName().equals(cookie.getName())) {
                return safeExtractUsername(cookie.getValue());
            }
        }
        return null;
    }

    private String safeExtractUsername(String token) {
        try {
            return jwtService.extractUsername(token);
        } catch (Exception ignored) {
            // token invalido -> handshake sin principal
            return null;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // no-op
    }
}
