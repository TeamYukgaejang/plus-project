package org.example.plusproject.domain.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.product.dto.request.ProductCreateRequest;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.service.command.ProductCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/products")
public class AdminProductController {

    private final ProductCommandService productCommandService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest productCreateRequest) {

        ProductResponse productResponse = productCommandService.createProduct(productCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.of(SuccessCode.PRODUCT_CREATED, productResponse));
    }
}
