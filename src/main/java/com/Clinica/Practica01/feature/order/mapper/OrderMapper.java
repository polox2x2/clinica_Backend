package com.Clinica.Practica01.feature.order.mapper;

import com.Clinica.Practica01.core.mapper.BaseMapper;
import com.Clinica.Practica01.feature.order.dto.OrderItemResponse;
import com.Clinica.Practica01.feature.order.dto.OrderRequest;
import com.Clinica.Practica01.feature.order.dto.OrderResponse;
import com.Clinica.Practica01.feature.order.entity.Order;
import com.Clinica.Practica01.feature.order.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/** Mapea Order. La creacion (stock/total) la maneja el service. */
@Component
public class OrderMapper implements BaseMapper<Order, OrderRequest, OrderResponse> {

    @Override
    public Order toEntity(OrderRequest r) {
        return Order.builder().total(BigDecimal.ZERO).build();
    }

    @Override
    public void updateEntity(Order e, OrderRequest r) {
        // Las ventas no se editan.
    }

    @Override
    public OrderResponse toResponse(Order e) {
        OrderResponse res = new OrderResponse();
        res.setTotal(e.getTotal());
        if (e.getPatient() != null) {
            res.setPatientId(e.getPatient().getId());
            if (e.getPatient().getUser() != null) {
                res.setPatientName(e.getPatient().getUser().getFirstName() + " "
                        + e.getPatient().getUser().getLastName());
            }
        }
        List<OrderItemResponse> items = e.getItems().stream().map(this::toItem).toList();
        res.setItems(items);
        return res;
    }

    private OrderItemResponse toItem(OrderItem i) {
        OrderItemResponse r = new OrderItemResponse();
        r.setQuantity(i.getQuantity());
        r.setUnitPrice(i.getUnitPrice());
        r.setSubtotal(i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
        if (i.getProduct() != null) {
            r.setProductId(i.getProduct().getId());
            r.setProductName(i.getProduct().getName());
        }
        return r;
    }
}
