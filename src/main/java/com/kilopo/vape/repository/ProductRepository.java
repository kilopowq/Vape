package com.kilopo.vape.repository;


import com.kilopo.vape.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
