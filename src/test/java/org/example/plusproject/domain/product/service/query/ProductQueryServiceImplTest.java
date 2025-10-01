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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductQueryServiceImpl productQueryService;

    // ===== getProductById 테스트 =====

    @Test
    void getProductById_성공시_응답반환() {
        // Given
        Long productId = 1L;
        Category category = Category.builder()
                .id(1L)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        Product product = Product.of("강아지 사료", 15000, "영양 만점 사료", category);
        ReflectionTestUtils.setField(product, "id", productId);
        Product spiedProduct = spy(product);  // ✨ Spy로 변경

        given(productRepository.findById(productId))
                .willReturn(Optional.of(spiedProduct));

        // When
        ProductResponse response = productQueryService.getProductById(productId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(productId);
        assertThat(response.getName()).isEqualTo("강아지 사료");
        assertThat(response.getPrice()).isEqualTo(15000);
        assertThat(response.getContent()).isEqualTo("영양 만점 사료");
        assertThat(response.getCategoryId()).isEqualTo(1L);

        // ✨ 조회수 증가 메서드 호출 확인
        verify(spiedProduct, times(1)).increaseViewCount();
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductById_상품없으면_예외발생() {
        // Given
        Long invalidProductId = 999L;

        given(productRepository.findById(invalidProductId))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productQueryService.getProductById(invalidProductId))
                .isInstanceOf(ProductException.class)
                .hasMessage(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());

        verify(productRepository, times(1)).findById(invalidProductId);
    }

    // ===== getRelatedProducts 테스트 (리뷰순) =====

    @Test
    void getRelatedProducts_리뷰순_성공() {
        // Given
        Long productId = 1L;
        String sort = "review";  // ✨ 추가
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
        List<ProductResponse> results = productQueryService.getRelatedProducts(productId, sort, limit);  // ✨ sort 추가

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

    // ===== getRelatedProducts 테스트 (조회수순) =====

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

    // ===== 예외 케이스 테스트 =====

    @Test
    void getRelatedProducts_기준상품없으면_예외발생() {
        // Given
        Long invalidProductId = 999L;
        String sort = "review";  // ✨ 추가
        int limit = 5;

        given(productRepository.findById(invalidProductId))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productQueryService.getRelatedProducts(invalidProductId, sort, limit))  // ✨ sort 추가
                .isInstanceOf(ProductException.class)
                .hasMessage(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());

        verify(productRepository, times(1)).findById(invalidProductId);
        verify(productRepository, times(0)).findByCategoryIdAndIdNotOrderByReviewCountDesc(
                any(), any(), any()  // ✨ null 대신 any() 사용
        );
    }

    @Test
    void getRelatedProducts_연관상품이없어도_빈리스트반환() {
        // Given
        Long productId = 1L;
        String sort = "review";  // ✨ 추가
        int limit = 5;

        Category category = Category.builder()
                .id(1L)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        Product baseProduct = Product.of("강아지 사료", 15000, "영양 만점 사료", category);
        ReflectionTestUtils.setField(baseProduct, "id", productId);

        Pageable pageable = PageRequest.of(0, limit);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(baseProduct));

        given(productRepository.findByCategoryIdAndIdNotOrderByReviewCountDesc(
                category.getId(),
                productId,
                pageable
        )).willReturn(List.of());

        // When
        List<ProductResponse> results = productQueryService.getRelatedProducts(productId, sort, limit);  // ✨ sort 추가

        // Then
        assertThat(results).isNotNull();
        assertThat(results).isEmpty();

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).findByCategoryIdAndIdNotOrderByReviewCountDesc(
                category.getId(),
                productId,
                pageable
        );
    }

    // ===== 추가 테스트: 잘못된 sort 파라미터 =====

    @Test
    void getRelatedProducts_잘못된정렬기준은_리뷰순으로_동작() {
        // Given
        Long productId = 1L;
        String sort = "invalid";  // ✨ 잘못된 값
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

        Pageable pageable = PageRequest.of(0, limit);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(baseProduct));

        given(productRepository.findByCategoryIdAndIdNotOrderByReviewCountDesc(
                category.getId(),
                productId,
                pageable
        )).willReturn(List.of(relatedProduct1));

        // When
        List<ProductResponse> results = productQueryService.getRelatedProducts(productId, sort, limit);

        // Then
        assertThat(results).hasSize(1);

        // ✨ default가 review이므로 리뷰순 메서드가 호출되어야 함
        verify(productRepository, times(1))
                .findByCategoryIdAndIdNotOrderByReviewCountDesc(category.getId(), productId, pageable);
        verify(productRepository, never())
                .findByCategoryIdAndIdNotOrderByViewCountDesc(any(), any(), any());
    }
}