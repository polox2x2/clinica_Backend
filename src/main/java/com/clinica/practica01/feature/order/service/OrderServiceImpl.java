package com.clinica.practica01.feature.order.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.core.service.AbstractCrudService;
import com.clinica.practica01.feature.order.dto.OrderItemRequest;
import com.clinica.practica01.feature.order.dto.OrderRequest;
import com.clinica.practica01.feature.order.dto.OrderResponse;
import com.clinica.practica01.feature.order.entity.Order;
import com.clinica.practica01.feature.order.entity.OrderItem;
import com.clinica.practica01.feature.order.mapper.OrderMapper;
import com.clinica.practica01.feature.order.repository.OrderRepository;
import com.clinica.practica01.feature.patient.repository.PatientRepository;
import com.clinica.practica01.feature.product.entity.Product;
import com.clinica.practica01.feature.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl
        extends AbstractCrudService<Order, OrderRequest, OrderResponse>
        implements OrderService {

    private final ProductRepository productRepository;
    private final PatientRepository patientRepository;

    public OrderServiceImpl(OrderRepository repository, OrderMapper mapper,
                            ProductRepository productRepository, PatientRepository patientRepository) {
        super(repository, mapper);
        this.productRepository = productRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    protected String resourceName() {
        return "Order";
    }

    /** Registra la venta: valida y descuenta stock, calcula el total. */
    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
        Order order = Order.builder().total(BigDecimal.ZERO).build();
        order.setActive(true);
        if (request.getPatientId() != null) {
            patientRepository.findById(request.getPatientId()).ifPresent(order::setPatient);
        }

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .filter(Product::isActive)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado: " + itemReq.getProductId()));

            if (product.getStock() < itemReq.getQuantity()) {
                throw new BusinessException(
                        "Stock insuficiente para " + product.getName()
                                + " (disponible: " + product.getStock() + ")", HttpStatus.CONFLICT);
            }
            product.setStock(product.getStock() - itemReq.getQuantity());
            productRepository.save(product);

            BigDecimal unitPrice = product.getPrice();
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity())));

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(unitPrice)
                    .build();
            item.setActive(true);
            order.getItems().add(item);
        }
        order.setTotal(total);
        return mapper.toResponseWithBase(repository.save(order));
    }
}
