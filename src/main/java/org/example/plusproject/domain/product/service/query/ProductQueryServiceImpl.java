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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final StringRedisTemplate redisTemplate;

    // 조회수 증가 방지 시간 (1시간)
    private static final long VIEW_COUNT_PREVENTION_DURATION = 1;
    private static final TimeUnit VIEW_COUNT_PREVENTION_UNIT = TimeUnit.HOURS;

    // 상품 상세 조회
    @Override
    @Transactional
    public ProductResponse getProductById(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 어뷰징 방지: 중복 조회 확인
        if (shouldIncreaseViewCount(productId, userId)) {
            product.increaseViewCount();
            log.info("조회수 증가: productId={}, userId={}, viewCount={}",
                    productId, userId, product.getViewCount());
        } else {
            log.debug("중복 조회: productId={}, userId={} (조회수 증가 안 함)",
                    productId, userId);
        }

        return ProductResponse.from(product);
    }

    /**
     * 조회수 증가 가능 여부 확인 (1시간 중복 방지)
     */
    private boolean shouldIncreaseViewCount(Long productId, Long userId) {
        // Redis Key 생성
        String key = userId != null
                ? String.format("product:view:%d:%d", productId, userId)
                : String.format("product:view:%d:anonymous", productId);

        // Redis에서 조회 기록 확인
        Boolean hasViewed = redisTemplate.hasKey(key);

        if (Boolean.TRUE.equals(hasViewed)) {
            return false;  // 이미 조회함
        }

        // 조회 기록 저장 (TTL: 1시간)
        redisTemplate.opsForValue().set(
                key,
                "1",
                VIEW_COUNT_PREVENTION_DURATION,
                VIEW_COUNT_PREVENTION_UNIT
        );
        return true;
    }

    // 인기 상품 조회
    @Override
    @Cacheable(
            value = "relatedProducts",
            key = "#productId + '_' + #sort + '_' + #limit"
    )
    public List<ProductResponse> getRelatedProducts(Long productId, String sort, int limit) {
        log.info("===== DB 조회 발생! productId={}, sort={}, limit={} =====",
                productId, sort, limit);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        Pageable pageable = PageRequest.of(0, limit);
        List<Product> relatedProducts;

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