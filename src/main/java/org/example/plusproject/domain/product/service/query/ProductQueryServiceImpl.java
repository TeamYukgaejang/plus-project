package org.example.plusproject.domain.product.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.exception.ProductErrorCode;
import org.example.plusproject.domain.product.exception.ProductException;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;

    // 상품 상세 조회
    @Override
    @Transactional
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 조회수 증가
        product.increaseViewCount();

        return ProductResponse.from(product);
    }

    // 인기 상품 조회
    @Override
    @Cacheable(
            value = "relatedProducts",  // Redis에 저장
            key = "#productId + '_' + #sort + '_' + #limit"
    )
    public List<ProductResponse> getRelatedProducts(Long productId, String sort, int limit) {
        log.info("===== DB 조회 발생! productId={}, limit={} =====",
                productId, sort, limit);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        Pageable pageable = PageRequest.of(0, limit);
        List<Product> relatedProducts;

        // switch 문으로 정렬 기준 분기
        switch (sort.toLowerCase()) {
            case "view":
                relatedProducts = productRepository
                        .findByCategoryIdAndIdNotOrderByViewCountDesc(
                                product.getCategory().getId(),
                                product.getId(),
                                pageable
                        );
                break;
            case "review":
            default:
                relatedProducts = productRepository
                        .findByCategoryIdAndIdNotOrderByReviewCountDesc(
                                product.getCategory().getId(),
                                product.getId(),
                                pageable
                        );
                break;
        }

        return relatedProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}