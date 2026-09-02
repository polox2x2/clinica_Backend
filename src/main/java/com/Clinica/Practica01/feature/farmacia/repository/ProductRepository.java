package com.Clinica.Practica01.feature.farmacia.repository;

import com.Clinica.Practica01.feature.farmacia.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
