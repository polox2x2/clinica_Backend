package com.clinica.practica01.core.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class CookieServiceTest {

    @Test
    void buildAndClear_useConfiguredSecurityAttributesAndDomain() {
        CookieService service = configured("auth_token", true, "None", "clinica.test", 3600);

        var cookie = service.build("jwt-value");
        assertThat(service.getCookieName()).isEqualTo("auth_token");
        assertThat(cookie.getValue()).isEqualTo("jwt-value");
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.isSecure()).isTrue();
        assertThat(cookie.getSameSite()).isEqualTo("None");
        assertThat(cookie.getDomain()).isEqualTo("clinica.test");
        assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(3600);

        var cleared = service.clear();
        assertThat(cleared.getValue()).isEmpty();
        assertThat(cleared.getMaxAge().isZero()).isTrue();
    }

    @Test
    void build_omitsBlankOrNullDomain() {
        assertThat(configured("token", false, "Lax", " ", 60).build("x").getDomain()).isNull();
        assertThat(configured("token", false, "Lax", null, 60).build("x").getDomain()).isNull();
    }

    private CookieService configured(String name, boolean secure, String sameSite, String domain, long maxAge) {
        CookieService service = new CookieService();
        ReflectionTestUtils.setField(service, "cookieName", name);
        ReflectionTestUtils.setField(service, "secure", secure);
        ReflectionTestUtils.setField(service, "sameSite", sameSite);
        ReflectionTestUtils.setField(service, "domain", domain);
        ReflectionTestUtils.setField(service, "maxAgeSeconds", maxAge);
        return service;
    }
}
