package org.example.plusproject.domain.product.service.query;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.exception.ProductErrorCode;
import org.example.plusproject.domain.product.exception.ProductException;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;

    // 상품 상세 조회
    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        return ProductResponse.from(product);
    }

    // 인기 상품 조회(리뷰순)
    @Override
    public List<ProductResponse> getRelatedProducts(Long productId, int limit) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        Pageable pageable = PageRequest.of(0, limit);
        List<Product> relatedProducts = productRepository.findByCategoryIdAndIdNotOrderByReviewCountDesc(
              product.getCategory().getId(), product.getId(), pageable);

        return relatedProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
