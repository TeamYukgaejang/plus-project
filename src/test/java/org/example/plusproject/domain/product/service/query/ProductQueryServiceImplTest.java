package org.example.plusproject.domain.product.service.query;

import org.example.plusproject.domain.category.entity.Category;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.exception.ProductErrorCode;
import org.example.plusproject.domain.product.exception.ProductException;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private ProductQueryServiceImpl productQueryService;

    // getProductById 테스트

    @Test
    void getProductById_첫조회시_조회수증가() {
        // Given
        Long productId = 1L;
        Long userId = 123L;

        Category category = Category.builder()
                .id(1L)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        Product product = Product.of("강아지 사료", 15000, "영양 만점 사료", category);
        ReflectionTestUtils.setField(product, "id", productId);
        Product spiedProduct = spy(product);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(spiedProduct));

        // Redis 모킹 (조회 기록 없음)
        given(redisTemplate.hasKey(anyString())).willReturn(false);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        // When
        ProductResponse response = productQueryService.getProductById(productId, userId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(productId);

        // 조회수 증가 확인
        verify(spiedProduct, times(1)).increaseViewCount();
        verify(productRepository, times(1)).findById(productId);

        // Redis 저장 확인
        verify(valueOperations, times(1)).set(
                anyString(),
                eq("1"),
                eq(1L),
                eq(TimeUnit.HOURS)
        );
    }

    @Test
    void getProductById_중복조회시_조회수증가안함() {
        // Given
        Long productId = 1L;
        Long userId = 123L;

        Category category = Category.builder()
                .id(1L)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        Product product = Product.of("강아지 사료", 15000, "영양 만점 사료", category);
        ReflectionTestUtils.setField(product, "id", productId);
        Product spiedProduct = spy(product);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(spiedProduct));

        // Redis 모킹 (조회 기록 있음)
        given(redisTemplate.hasKey(anyString())).willReturn(true);

        // When
        ProductResponse response = productQueryService.getProductById(productId, userId);

        // Then
        assertThat(response).isNotNull();

        // 조회수 증가 안 함
        verify(spiedProduct, never()).increaseViewCount();
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductById_상품없으면_예외발생() {
        // Given
        Long invalidProductId = 999L;
        Long userId = 123L;

        given(productRepository.findById(invalidProductId))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productQueryService.getProductById(invalidProductId, userId))
                .isInstanceOf(ProductException.class)
                .hasMessage(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());

        verify(productRepository, times(1)).findById(invalidProductId);
    }

    // getRelatedProducts 테스트 (리뷰순)

    @Test
    void getRelatedProducts_리뷰순_성공() {
        // Given
        Long productId = 1L;
        String sort = "review";
        int limit = 5;

        Category category = Category.builder()
                .id(1L)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        Product baseProduct = Product.of("강아지 사료", 15000, "영양 만점 사료", category);
        ReflectionTestUtils.setField(baseProduct, "id", productId);

        Product relatedProduct1 = Product.of("고양이 사료", 12000, "맛있는 사료", category);
        ReflectionTestUtils.setField(relatedProduct1, "id", 2L);
        ReflectionTestUtils.setField(relatedProduct1, "reviewCount", 100);

        Product relatedProduct2 = Product.of("햄스터 사료", 8000, "건강한 사료", category);
        ReflectionTestUtils.setField(relatedProduct2, "id", 3L);
        ReflectionTestUtils.setField(relatedProduct2, "reviewCount", 80);

        Pageable pageable = PageRequest.of(0, limit);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(baseProduct));

        given(productRepository.findByCategoryIdAndIdNotOrderByReviewCountDesc(
                category.getId(),
                productId,
                pageable
        )).willReturn(List.of(relatedProduct1, relatedProduct2));

        // When
        List<ProductResponse> results = productQueryService.getRelatedProducts(productId, sort, limit);

        // Then
        assertThat(results).isNotNull();
        assertThat(results).hasSize(2);

        ProductResponse first = results.get(0);
        assertThat(first.getId()).isEqualTo(2L);
        assertThat(first.getName()).isEqualTo("고양이 사료");
        assertThat(first.getReviewCount()).isEqualTo(100);

        ProductResponse second = results.get(1);
        assertThat(second.getId()).isEqualTo(3L);
        assertThat(second.getName()).isEqualTo("햄스터 사료");
        assertThat(second.getReviewCount()).isEqualTo(80);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).findByCategoryIdAndIdNotOrderByReviewCountDesc(
                category.getId(),
                productId,
                pageable
        );
    }

    // getRelatedProducts 테스트 (조회수순)

    @Test
    void getRelatedProducts_조회수순_성공() {
        // Given
        Long productId = 1L;
        String sort = "view";
        int limit = 5;

        Category category = Category.builder()
                .id(1L)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        Product baseProduct = Product.of("강아지 사료", 15000, "영양 만점 사료", category);
        ReflectionTestUtils.setField(baseProduct, "id", productId);

        Product relatedProduct1 = Product.of("고양이 사료", 12000, "맛있는 사료", category);
        ReflectionTestUtils.setField(relatedProduct1, "id", 2L);
        ReflectionTestUtils.setField(relatedProduct1, "viewCount", 500);

        Product relatedProduct2 = Product.of("햄스터 사료", 8000, "건강한 사료", category);
        ReflectionTestUtils.setField(relatedProduct2, "id", 3L);
        ReflectionTestUtils.setField(relatedProduct2, "viewCount", 300);

        Pageable pageable = PageRequest.of(0, limit);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(baseProduct));

        given(productRepository.findByCategoryIdAndIdNotOrderByViewCountDesc(
                category.getId(),
                productId,
                pageable
        )).willReturn(List.of(relatedProduct1, relatedProduct2));

        // When
        List<ProductResponse> results = productQueryService.getRelatedProducts(productId, sort, limit);

        // Then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getViewCount()).isEqualTo(500);
        assertThat(results.get(1).getViewCount()).isEqualTo(300);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1))
                .findByCategoryIdAndIdNotOrderByViewCountDesc(category.getId(), productId, pageable);
    }
}