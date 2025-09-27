package org.example.plusproject.domain.product.service.command;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.product.dto.request.ProductCreateRequest;
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
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {

        Product product = Product.of(
                productCreateRequest.getName(),
                productCreateRequest.getPrice(),
                productCreateRequest.getContent(),
                productCreateRequest.getCategoryId()
        );

        Product savedProduct = productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }
}
