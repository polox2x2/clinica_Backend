package com.clinica.practica01.feature.user.service;

import com.clinica.practica01.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

/**
 * Genera el username: primer nombre + "." + primer apellido + inicial del
 * segundo apellido (minusculas, sin tildes). Ej: "Luis Enrique Martinez
 * Quijandria" -> "luis.martinezq". Garantiza unicidad agregando un numero.
 */
@Component
@RequiredArgsConstructor
public class UsernameGenerator {

    private final UserRepository userRepository;

    public String generate(String firstName, String lastName) {
        String[] names = safeTokens(firstName);
        String[] surnames = safeTokens(lastName);

        String given = names.length > 0 ? names[0] : "user";
        String surname1 = surnames.length > 0 ? surnames[0] : "";
        String surname2Initial = surnames.length > 1 && !surnames[1].isEmpty()
                ? surnames[1].substring(0, 1)
                : "";

        String base = normalize(given) + "." + normalize(surname1) + normalize(surname2Initial);
        base = base.replaceAll("\\.$", ""); // por si no hay apellido

        return ensureUnique(base);
    }

    private String ensureUnique(String base) {
        if (!userRepository.existsByUsername(base)) {
            return base;
        }
        int i = 2;
        while (userRepository.existsByUsername(base + i)) {
            i++;
        }
        return base + i;
    }

    private String[] safeTokens(String value) {
        if (value == null || value.isBlank()) {
            return new String[0];
        }
        return value.trim().split("\\s+");
    }

    /** Minusculas, sin tildes, solo alfanumerico. */
    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        String noAccents = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noAccents.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}
