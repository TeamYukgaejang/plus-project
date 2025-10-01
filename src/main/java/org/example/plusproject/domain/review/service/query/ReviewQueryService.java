package org.example.plusproject.domain.review.service.query;

import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.review.dto.response.ReviewPageResponse;
import org.example.plusproject.domain.review.dto.response.ReviewResponse;
import org.example.plusproject.domain.review.entity.Review;
import org.springframework.data.domain.Page;

public interface ReviewQueryService {

    ApiResponse<ReviewPageResponse> getPageReview(int page, int size, Long productId);
    ApiResponse<ReviewResponse> getReview(Long reviewId);

    Review findReviewById(Long reviewId);
    Double getAvgPoint(Long productId);
}
