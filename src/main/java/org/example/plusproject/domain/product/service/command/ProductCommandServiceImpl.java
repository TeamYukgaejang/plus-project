package org.example.plusproject.domain.product.service.command;

import org.example.plusproject.domain.category.entity.Category;
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

        // 상품 설명 필수 검증
        if (productRequest.getContent() == null || productRequest.getContent().trim().isEmpty()) {
            throw new ProductException(ProductErrorCode.INVALID_PRODUCT_CONTENT);
        }

        // 상품 가격 0원 이상 검증
        if (productRequest.getPrice() == null || productRequest.getPrice() <= 0) {
            throw new ProductException(ProductErrorCode.INVALID_PRODUCT_PRICE);
        }

        Category category = productRequest.getCategory();
        Long categoryId = category.getId();

        Product product = Product.of(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getContent(),
                category
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
                productRequest.getCategory()
        );

        return ProductResponse.from(product);
    }

    // 상품 삭제
    @Override
    @Transactional
    public ProductResponse deleteProduct(Long productId) {
        Product product = productRepository.findByIdIncludingDeleted(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        if (product.getDeletedAt() != null) {
            throw new ProductException(ProductErrorCode.PRODUCT_ALREADY_DELETED);
        }

        product.delete(); // deletedAt에 현재 시간 설정

        return ProductResponse.from(product);
    }
}
