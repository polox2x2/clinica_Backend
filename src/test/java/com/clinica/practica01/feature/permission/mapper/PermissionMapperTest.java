package com.clinica.practica01.feature.permission.mapper;

import com.clinica.practica01.feature.permission.dto.PermissionRequest;
import com.clinica.practica01.feature.permission.entity.Permission;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PermissionMapperTest {

    private final PermissionMapper mapper = new PermissionMapper();

    @Test
    void roundTrip() {
        PermissionRequest req = new PermissionRequest();
        req.setName("User:Read");
        req.setGroupName("User");
        req.setDescription("Read User");

        Permission entity = mapper.toEntity(req);
        assertThat(entity.getName()).isEqualTo("User:Read");
        assertThat(entity.getGroupName()).isEqualTo("User");

        req.setDescription("changed");
        mapper.updateEntity(entity, req);
        assertThat(entity.getDescription()).isEqualTo("changed");

        var res = mapper.toResponse(entity);
        assertThat(res.getName()).isEqualTo("User:Read");
        assertThat(res.getGroupName()).isEqualTo("User");
    }
}
