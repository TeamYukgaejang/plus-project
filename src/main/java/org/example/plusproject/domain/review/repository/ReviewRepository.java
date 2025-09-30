package org.example.plusproject.domain.review.repository;

import org.example.plusproject.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "JOIN r.user u" + " JOIN r.product p" +
            " WHERE r.deletedAt IS NULL"
    )
    Optional<Review> findByIdWithUserAndProduct(Long reviewId);
}
