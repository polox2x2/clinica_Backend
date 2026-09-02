package com.Clinica.Practica01.feature.farmacia.repository;

import com.Clinica.Practica01.feature.farmacia.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
