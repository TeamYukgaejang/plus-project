package org.example.plusproject.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.exception.ProductSuccessCode;
import org.example.plusproject.domain.product.service.query.ProductQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
