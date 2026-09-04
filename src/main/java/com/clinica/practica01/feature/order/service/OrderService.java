package com.clinica.practica01.feature.order.service;

import com.clinica.practica01.core.service.CrudService;
import com.clinica.practica01.feature.order.dto.OrderRequest;
import com.clinica.practica01.feature.order.dto.OrderResponse;

public interface OrderService extends CrudService<OrderRequest, OrderResponse> {
}
