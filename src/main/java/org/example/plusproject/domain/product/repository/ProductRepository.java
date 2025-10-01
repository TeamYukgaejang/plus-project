package org.example.plusproject.domain.product.repository;

import org.example.plusproject.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // name 기준 LIKE 검색
    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    // 리뷰순
    @EntityGraph(attributePaths = {"category"})
    List<Product> findByCategoryIdAndIdNotOrderByReviewCountDesc(
            Long categoryId,
            Long excludedProductId,
            Pageable pageable
    );

    // 조회순
    @EntityGraph(attributePaths = {"category"})
    List<Product> findByCategoryIdAndIdNotOrderByViewCountDesc(
            Long categoryId,
            Long excludedProductId,
            Pageable pageable
    );

    // 조회수 증가 쿼리 메서드 추가
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = p.viewCount + 1 WHERE p.id = :productId")
    void increaseViewCount(@Param("productId") Long productId);

    @Query("""
                SELECT p
                FROM Product p
                WHERE p.id = :productId
            """)
    Optional<Product> findByIdIncludingDeleted(@Param("productId") Long productId);
}