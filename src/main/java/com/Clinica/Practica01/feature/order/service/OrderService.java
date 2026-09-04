package com.Clinica.Practica01.feature.order.service;

import com.Clinica.Practica01.core.service.CrudService;
import com.Clinica.Practica01.feature.order.dto.OrderRequest;
import com.Clinica.Practica01.feature.order.dto.OrderResponse;

public interface OrderService extends CrudService<OrderRequest, OrderResponse> {
}
