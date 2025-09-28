package org.example.plusproject.domain.review.service.command;

import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.review.dto.request.ReviewSaveRequest;
import org.example.plusproject.domain.review.dto.response.ReviewSaveResponse;
import org.example.plusproject.domain.user.dto.security.AuthUser;

public interface ReviewCommandService {

    ApiResponse<ReviewSaveResponse> saveReview(
            ReviewSaveRequest request,
            AuthUser authUser,
            Long productId);

}
