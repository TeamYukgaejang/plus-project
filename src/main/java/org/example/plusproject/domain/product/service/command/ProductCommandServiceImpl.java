package org.example.plusproject.domain.product.service.command;

import org.example.plusproject.domain.category.entity.Category;
import org.example.plusproject.domain.category.exception.CategoryErrorCode;
import org.example.plusproject.domain.category.exception.CategoryException;
import org.example.plusproject.domain.category.repository.CategoryRepository;
import org.example.plusproject.domain.product.exception.ProductErrorCode;
import org.example.plusproject.domain.product.exception.ProductException;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final CategoryRepository categoryRepository;

    // 상품 등록
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse createProduct(ProductRequest productRequest) {

        Long categoryId = productRequest.getCategoryId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

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
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.update(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getContent(),
                product.getCategory()
        );

        return ProductResponse.from(product);
    }

    // 상품 삭제
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
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
