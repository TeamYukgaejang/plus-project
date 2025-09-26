package org.example.plusproject.domain.product.repository;

import org.example.plusproject.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    // name 기준 LIKE 검색
    Page<Product> findByNameContaining(String keyword, Pageable pageable);
}
