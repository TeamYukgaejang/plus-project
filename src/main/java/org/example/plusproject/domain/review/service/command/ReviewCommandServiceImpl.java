package org.example.plusproject.domain.review.service.command;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.exception.GlobalException;
import org.example.plusproject.domain.like.service.query.LikeQueryServiceImpl;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.exception.ProductErrorCode;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.example.plusproject.domain.review.consts.ReviewErrorCode;
import org.example.plusproject.domain.review.consts.ReviewSuccessCode;
import org.example.plusproject.domain.review.dto.request.ReviewSaveRequest;
import org.example.plusproject.domain.review.dto.response.ReviewResponse;
import org.example.plusproject.domain.review.entity.Review;
import org.example.plusproject.domain.review.repository.ReviewRepository;
import org.example.plusproject.domain.user.dto.security.AuthUser;
import org.example.plusproject.domain.user.entity.User;
import org.example.plusproject.domain.user.service.query.UserQueryServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final LikeQueryServiceImpl likeService;
    private final UserQueryServiceImpl userService;
    private final ProductRepository  productRepository;

    @Override
    @Transactional
    public ApiResponse<ReviewResponse> saveReview(ReviewSaveRequest request, AuthUser authUser, Long productId) {
        User user = userService.findUserById(authUser.getUserId());
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new GlobalException(ProductErrorCode.PRODUCT_NOT_FOUND)
        );

        Review savedReview = reviewRepository.save(
                Review.of(
                        request.getContext(),
                        request.getPoint(),
                        user,
                        product
                )
        );

        return ApiResponse.of(
                ReviewSuccessCode.REVIEW_CREATED,
                ReviewResponse.of(
                        savedReview,
                        likeService.getLikeCount(savedReview.getId())
                )
        );
    }

    @Override
    @Transactional
    public ApiResponse<ReviewResponse> updateReview(ReviewSaveRequest request, Long reviewId, AuthUser authUser) {

        Review review = reviewRepository.findByIdWithUserAndProduct(reviewId).orElseThrow(
                () -> new GlobalException(ReviewErrorCode.REVIEW_NOT_FOUND)
        );

        if (!review.getUser().getId().equals(authUser.getUserId())) {
            throw new GlobalException(ReviewErrorCode.REVIEW_FORBIDDEN);
        }

        review.update(request.getContext(), request.getPoint());

        return ApiResponse.of(
                ReviewSuccessCode.REVIEW_UPDATED,
                ReviewResponse.of(
                        review,
                        likeService.getLikeCount(review.getId())
                )
        );
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteReview(Long reviewId, AuthUser authUser) {
        Review review = reviewRepository.findByIdWithUserAndProduct(reviewId).orElseThrow(
                () -> new GlobalException(ReviewErrorCode.REVIEW_NOT_FOUND)
        );

        if (!review.getUser().getId().equals(authUser.getUserId())) {
            throw new GlobalException(ReviewErrorCode.REVIEW_FORBIDDEN);
        }

        review.delete();

        return ApiResponse.of(ReviewSuccessCode.REVIEW_DELETED, null);
    }

}