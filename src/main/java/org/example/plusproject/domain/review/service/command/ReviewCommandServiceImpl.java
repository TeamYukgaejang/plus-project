package org.example.plusproject.domain.review.service.command;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.service.query.ProductQueryServiceImpl;
import org.example.plusproject.domain.review.dto.request.ReviewSaveRequest;
import org.example.plusproject.domain.review.dto.response.ReviewSaveResponse;
import org.example.plusproject.domain.review.entity.Review;
import org.example.plusproject.domain.review.repository.ReviewRepository;
import org.example.plusproject.domain.user.dto.security.AuthUser;
import org.example.plusproject.domain.user.entity.User;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;

    // 존재를... 거... 확인할... 메서드.. 추가... 부탁드립니다...
    private final UserQueryServiceImpl userService;
    private final ProductQueryServiceImpl productService;

    @Override
    public ApiResponse<ReviewSaveResponse> saveReview(ReviewSaveRequest request, AuthUser authUser, Long productId) {
        User user = userService.getUserById(authUser.getUserId());
        Product product = productService.getProductById(productId);

        Review SavedReview = reviewRepository.save(Review.of(request.getContext(), request.getPoint(), user, product));

        return ApiResponse.of(SuccessCode.REQUEST_SUCCESS, ReviewSaveResponse.of(SavedReview, product, user));
    }
}