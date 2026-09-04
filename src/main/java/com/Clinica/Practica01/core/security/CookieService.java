package com.Clinica.Practica01.core.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

/**
 * Construye la cookie httpOnly que transporta el JWT. Configurable por entorno
 * (SameSite/Secure/Domain) para funcionar tanto en local como cross-subdominio.
 */
@Service
public class CookieService {

    @Value("${app.auth.cookie.name:access_token}")
    private String cookieName;

    @Value("${app.auth.cookie.secure:false}")
    private boolean secure;

    @Value("${app.auth.cookie.same-site:Lax}")
    private String sameSite;

    @Value("${app.auth.cookie.domain:}")
    private String domain;

    @Value("${app.auth.cookie.max-age-seconds:86400}")
    private long maxAgeSeconds;

    public String getCookieName() {
        return cookieName;
    }

    /** Cookie con el token (login). */
    public ResponseCookie build(String token) {
        return base(token, maxAgeSeconds);
    }

    /** Cookie vacia con maxAge 0 (logout). */
    public ResponseCookie clear() {
        return base("", 0);
    }

    private ResponseCookie base(String value, long maxAge) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(cookieName, value)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite(sameSite)
                .maxAge(maxAge);
        if (domain != null && !domain.isBlank()) {
            b.domain(domain);
        }
        return b.build();
    }
}
