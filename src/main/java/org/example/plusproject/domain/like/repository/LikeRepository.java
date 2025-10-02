package org.example.plusproject.domain.like.repository;

import org.example.plusproject.domain.like.entity.Like;
import org.example.plusproject.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByReviewIdAndUserId(Long reviewId, Long userId);
    Long countByReviewId(Long reviewId);

    void deleteAllByReview(Review review);
}
