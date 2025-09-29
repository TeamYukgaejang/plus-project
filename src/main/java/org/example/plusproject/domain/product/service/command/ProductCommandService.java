package org.example.plusproject.domain.product.service.command;

import org.example.plusproject.domain.product.dto.request.ProductRequest;
import org.example.plusproject.domain.product.dto.response.ProductResponse;

public interface ProductCommandService {
    ProductResponse createProduct(ProductRequest productCreateRequest);
    ProductResponse updateProduct(Long productId, ProductRequest productUpdateRequest);
    ProductResponse deleteProduct(Long productId);
}
