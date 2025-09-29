package org.example.plusproject.domain.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.product.dto.request.ProductRequest;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.exception.ProductSuccessCode;
import org.example.plusproject.domain.product.service.command.ProductCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/products")
public class AdminProductController {

    private final ProductCommandService productCommandService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest productRequest
    ) {
        ProductResponse productResponse = productCommandService.createProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.of(ProductSuccessCode.PRODUCT_CREATED, productResponse));
    }

    // 상품 정보 수정
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest productRequest
    ) {
        ProductResponse productResponse = productCommandService.updateProduct(productId, productRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(ProductSuccessCode.PRODUCT_UPDATED, productResponse));
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> deleteProduct(
            @PathVariable Long productId
    ){
        ProductResponse productResponse = productCommandService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.of(ProductSuccessCode.PRODUCT_DELETED, productResponse));
    }
}
