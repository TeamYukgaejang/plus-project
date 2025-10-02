package org.example.plusproject.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.review.dto.request.ReviewSaveRequest;
import org.example.plusproject.domain.review.dto.response.ReviewPageResponse;
import org.example.plusproject.domain.review.dto.response.ReviewResponse;
import org.example.plusproject.domain.review.service.command.ReviewCommandService;
import org.example.plusproject.domain.review.service.query.ReviewQueryService;
import org.example.plusproject.domain.user.dto.security.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    // 리뷰 저장
    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> saveReview(
            @Valid @RequestBody ReviewSaveRequest request,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long productId
    ) {
        return ResponseEntity
                .status(201)
                .body(reviewCommandService.saveReview(request, authUser, productId));
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<ApiResponse<ReviewPageResponse>> getPageReview(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long productId
    ) {
        return ResponseEntity
                .status(200)
                .body(reviewQueryService.getPageReview(page, size, productId));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReview(
            @PathVariable Long reviewId
    ) {
        return ResponseEntity
                .status(200)
                .body(reviewQueryService.getReview(reviewId));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @Valid @RequestBody ReviewSaveRequest request,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity
                .status(200)
                .body(reviewCommandService.updateReview(request, reviewId, authUser));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity
                .status(204)
                .body(reviewCommandService.deleteReview(reviewId, authUser));
    }
}