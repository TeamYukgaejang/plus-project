package org.example.plusproject.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.exception.ProductSuccessCode;
import org.example.plusproject.domain.product.service.query.ProductQueryService;
import org.example.plusproject.domain.user.dto.security.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductQueryService productQueryService;

    // 상품 상세 조회 (어뷰징 방지 적용)
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        // 로그인 사용자면 userId 전달, 아니면 null
        Long userId = (authUser != null) ? authUser.getUserId() : null;

        ProductResponse productResponse =
                productQueryService.getProductById(productId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(ProductSuccessCode.PRODUCT_GET_SUCCESS, productResponse));
    }

    // 인기 상품 조회 (정렬 기준: review 또는 view)
    @GetMapping("/{productId}/related")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getRelatedProducts(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "review") String sort,
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<ProductResponse> relatedProducts =
                productQueryService.getRelatedProducts(productId, sort, limit);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(ProductSuccessCode.PRODUCT_GET_SUCCESS, relatedProducts));
    }
}