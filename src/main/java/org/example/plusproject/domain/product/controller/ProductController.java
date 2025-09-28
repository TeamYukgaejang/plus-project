package org.example.plusproject.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.exception.ProductSuccessCode;
import org.example.plusproject.domain.product.service.query.ProductQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductQueryService productQueryService;

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long productId
    ) {
        ProductResponse productResponse = productQueryService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(ProductSuccessCode.PRODUCT_GET_SUCCESS, productResponse));
    }

    // 인기 상품 조회(리뷰순)
    @GetMapping("/{productId}/related")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getRelatedProductsByReview(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "review") String sort,
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<ProductResponse> relatedProducts = productQueryService.getRelatedProducts(productId, sort, limit);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(ProductSuccessCode.PRODUCT_GET_SUCCESS, relatedProducts));
    }
}
