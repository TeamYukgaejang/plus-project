package org.example.plusproject.domain.review.repository;

import org.example.plusproject.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "JOIN r.user u" + " JOIN r.product p" +
            " WHERE r.deletedAt IS NULL"
    )
    Optional<Review> findByIdWithUserAndProduct(Long reviewId);

    @EntityGraph(attributePaths = {"product","user"})
    Page<Review> findByProductIdAndDeletedAtIsNullOrderByUpdatedAtDesc(Long productId, Pageable pageable);

    @Query("SELECT AVG(r.point) FROM Review r WHERE r.product.id = :productId")
    Double findAvgPointByProductId(@Param("productId") Long productId);
}
