package org.example.plusproject.domain.product.service.query;

import org.example.plusproject.domain.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductQueryService {
    ProductResponse getProductById(Long productId);
    List<ProductResponse> getRelatedProducts(Long productId, int limit);
}
