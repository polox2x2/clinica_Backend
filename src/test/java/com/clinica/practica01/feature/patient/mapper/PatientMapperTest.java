package com.clinica.practica01.feature.patient.mapper;

import com.clinica.practica01.feature.patient.dto.PatientRequest;
import com.clinica.practica01.feature.patient.entity.Patient;
import com.clinica.practica01.feature.user.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PatientMapperTest {

    private final PatientMapper mapper = new PatientMapper();

    @Test
    void toEntityAndUpdate() {
        PatientRequest req = new PatientRequest();
        req.setDocumentId("123");
        req.setDateOfBirth(LocalDate.of(1990, 1, 1));
        req.setPhone("999");

        Patient e = mapper.toEntity(req);
        assertThat(e.getDocumentId()).isEqualTo("123");

        req.setPhone("888");
        mapper.updateEntity(e, req);
        assertThat(e.getPhone()).isEqualTo("888");
    }

    @Test
    void toResponse_includesUserData() {
        User u = new User();
        u.setId(UUID.randomUUID());
        u.setUsername("pat");
        u.setFirstName("Ana");
        u.setLastName("Gomez");
        u.setEmail("ana@x.com");
        Patient e = Patient.builder().documentId("123").phone("999").user(u).build();

        var res = mapper.toResponse(e);
        assertThat(res.getDocumentId()).isEqualTo("123");
        assertThat(res.getUsername()).isEqualTo("pat");
        assertThat(res.getEmail()).isEqualTo("ana@x.com");
    }

    @Test
    void toResponse_withoutUser() {
        Patient e = Patient.builder().documentId("123").build();
        assertThat(mapper.toResponse(e).getUsername()).isNull();
    }
}
