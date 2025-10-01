package org.example.plusproject.domain.review.service.query;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.exception.GlobalException;
import org.example.plusproject.domain.like.service.query.LikeQueryService;
import org.example.plusproject.domain.review.consts.ReviewErrorCode;
import org.example.plusproject.domain.review.consts.ReviewSuccessCode;
import org.example.plusproject.domain.review.dto.response.ReviewPageResponse;
import org.example.plusproject.domain.review.dto.response.ReviewResponse;
import org.example.plusproject.domain.review.entity.Review;
import org.example.plusproject.domain.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewRepository reviewRepository;
    private final LikeQueryService likeService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<ReviewPageResponse> getPageReview(int page, int size, Long productId) {
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size);

        Page<Review> reviews = reviewRepository
                .findByProductIdAndDeletedAtIsNotNullOrderByUpdatedAtDesc(productId, pageable);

        Page<ReviewResponse> pageResponse = reviews.map(review -> {
            Long likeCount = likeService.getLikeCount(review.getId());
            return ReviewResponse.builder()
                    .id(review.getId())
                    .point(review.getPoint())
                    .context(review.getContent())
                    .productId(review.getProduct().getId())
                    .productName(review.getProduct().getName())
                    .userId(review.getUser().getId())
                    .likes(likeCount)
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        });

        return ApiResponse.of(
                ReviewSuccessCode.REVIEW_GET_SUCCESS,
                ReviewPageResponse.of(
                        getAvgPoint(productId),
                        pageResponse
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<ReviewResponse> getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new GlobalException(ReviewErrorCode.REVIEW_NOT_FOUND)
        );
        return ApiResponse.of(
                ReviewSuccessCode.REVIEW_GET_SUCCESS,
                ReviewResponse.of(
                        review,
                        likeService.getLikeCount(review.getId())
                )
        );
    }

    @Override
    public Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new GlobalException(ReviewErrorCode.REVIEW_NOT_FOUND)
        );
    }

    // 캐시입니다!!!!!!!!!
    @Override
    public Double getAvgPoint(Long productId) {

        String redisKey = "product:" + productId + ":avgPoint";

        String cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            return Double.parseDouble(cached);
        }

        Double avgPoint = reviewRepository.findAvgPointByProductId(productId);
        if (avgPoint == null) {
            avgPoint = 0.0;
        }

        redisTemplate.opsForValue().set(redisKey, String.valueOf(avgPoint), Duration.ofMinutes(10));

        return avgPoint;
    }
}
