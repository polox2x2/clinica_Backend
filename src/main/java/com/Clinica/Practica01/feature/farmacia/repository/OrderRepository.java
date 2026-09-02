package com.Clinica.Practica01.feature.farmacia.repository;

import com.Clinica.Practica01.feature.farmacia.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
