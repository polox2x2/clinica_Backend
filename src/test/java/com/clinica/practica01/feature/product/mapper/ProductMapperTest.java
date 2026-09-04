package com.clinica.practica01.feature.product.mapper;

import com.clinica.practica01.feature.product.dto.ProductRequest;
import com.clinica.practica01.feature.product.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    void roundTrip() {
        ProductRequest req = new ProductRequest();
        req.setName("Paracetamol");
        req.setDescription("500mg");
        req.setPrice(new BigDecimal("2.50"));
        req.setStock(100);

        Product e = mapper.toEntity(req);
        assertThat(e.getName()).isEqualTo("Paracetamol");
        assertThat(e.getStock()).isEqualTo(100);

        req.setPrice(new BigDecimal("3.00"));
        mapper.updateEntity(e, req);
        assertThat(e.getPrice()).isEqualByComparingTo("3.00");

        var res = mapper.toResponse(e);
        assertThat(res.getName()).isEqualTo("Paracetamol");
        assertThat(res.getStock()).isEqualTo(100);
    }
}
