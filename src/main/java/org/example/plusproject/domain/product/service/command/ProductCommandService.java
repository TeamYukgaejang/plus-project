package org.example.plusproject.domain.product.service.command;

import org.example.plusproject.domain.product.dto.request.ProductCreateRequest;
import org.example.plusproject.domain.product.dto.response.ProductResponse;

public interface ProductCommandService {
    ProductResponse createProduct(ProductCreateRequest productCreateRequest);
}
