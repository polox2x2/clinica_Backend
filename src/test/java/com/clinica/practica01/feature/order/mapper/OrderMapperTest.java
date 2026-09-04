package com.clinica.practica01.feature.order.mapper;

import com.clinica.practica01.feature.order.dto.OrderRequest;
import com.clinica.practica01.feature.order.entity.Order;
import com.clinica.practica01.feature.order.entity.OrderItem;
import com.clinica.practica01.feature.patient.entity.Patient;
import com.clinica.practica01.feature.product.entity.Product;
import com.clinica.practica01.feature.user.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {

    private final OrderMapper mapper = new OrderMapper();

    @Test
    void toEntity_initializesZeroTotal() {
        assertThat(mapper.toEntity(new OrderRequest()).getTotal()).isEqualByComparingTo("0");
    }

    @Test
    void toResponse_mapsItemsAndPatient() {
        Product p = Product.builder().name("Pills").build();
        p.setId(UUID.randomUUID());
        OrderItem item = OrderItem.builder().quantity(2).unitPrice(new BigDecimal("5.00")).product(p).build();
        User u = new User();
        u.setFirstName("Ana");
        u.setLastName("Gomez");
        Patient patient = Patient.builder().user(u).build();
        patient.setId(UUID.randomUUID());
        Order order = Order.builder().total(new BigDecimal("10.00")).patient(patient).items(List.of(item)).build();

        var res = mapper.toResponse(order);
        assertThat(res.getTotal()).isEqualByComparingTo("10.00");
        assertThat(res.getItems()).hasSize(1);
        assertThat(res.getItems().get(0).getSubtotal()).isEqualByComparingTo("10.00");
        assertThat(res.getItems().get(0).getProductName()).isEqualTo("Pills");
        assertThat(res.getPatientName()).isEqualTo("Ana Gomez");
    }
}
