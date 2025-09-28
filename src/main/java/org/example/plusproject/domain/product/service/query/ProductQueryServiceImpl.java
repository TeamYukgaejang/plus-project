package org.example.plusproject.domain.product.service.query;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.exception.GlobalException;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.exception.ProductErrorCode;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GlobalException(ProductErrorCode.PRODUCT_NOT_FOUND));
        return ProductResponse.from(product);
    }

}
