package org.example.plusproject.domain.product.service.command;

import org.example.plusproject.common.exception.GlobalException;
import org.example.plusproject.domain.product.exception.ProductErrorCode;
import org.example.plusproject.domain.product.exception.ProductException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.product.dto.request.ProductRequest;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepository productRepository;

    // 상품 등록
    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {

        Product product = Product.of(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getContent(),
                productRequest.getCategoryId()
        );

        Product savedProduct = productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }

    // 상품 정보 수정
    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.update(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getContent(),
                productRequest.getCategoryId()
        );

        return ProductResponse.from(product);
    }
}
