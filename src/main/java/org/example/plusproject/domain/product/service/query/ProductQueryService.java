package org.example.plusproject.domain.product.service.query;

import org.example.plusproject.domain.product.dto.response.ProductResponse;

public interface ProductQueryService {
    ProductResponse getProductById(Long productId);
}
