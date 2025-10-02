package org.example.plusproject.domain.product.service.query;

import org.example.plusproject.domain.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductQueryService {
    // userId 파라미터 추가
    ProductResponse getProductById(Long productId, Long userId);

    List<ProductResponse> getRelatedProducts(Long productId, String sort, int limit);
}
