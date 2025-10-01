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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceImplTest {

    // 가짜 ProductRepository 생성
    @Mock
    private ProductRepository productRepository;

    // 테스트 대상: 진짜 ProductQueryServiceImpl (Mock 주입)
    @InjectMocks
    private ProductQueryServiceImpl productQueryService;


    // getProductById 테스트

    @Test
    void getProductById_성공시_응답반환() {
        // Given
        Long productId = 1L;

        // Category 생성
        Category category = Category.builder()
                .id(1L)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        // Product 생성
        Product product = Product.of(
                "강아지 사료",
                15000,
                "영양 만점 사료",
                category
        );
        ReflectionTestUtils.setField(product, "id", productId);

        // productRepository.findById() 호출되면 → product 반환
        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        // when
        ProductResponse response = productQueryService.getProductById(productId);

        // then
        assertThat(response).isNotNull();                                          // 응답이 null이 아님
        assertThat(response.getId()).isEqualTo(productId);                         // ID 확인
        assertThat(response.getName()).isEqualTo("강아지 사료");            // 이름 확인
        assertThat(response.getPrice()).isEqualTo(15000);                // 가격 확인
        assertThat(response.getContent()).isEqualTo("영양 만점 사료");      // 내용 확인
        assertThat(response.getCategoryId()).isEqualTo(1L);             // 카테고리 ID 확인

        // Mock 메서드 호출 검증
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductById_상품없으면_예외발생() {
        // Given
        Long invalidProductId = 999L;

        // 상품을 찾을 수 없음 (빈 Optional 반환)
        given(productRepository.findById(invalidProductId))
                .willReturn(Optional.empty());

        /* when & then
        ProductException이 발생하는지 확인
         */
        assertThatThrownBy(() -> productQueryService.getProductById(invalidProductId))
                .isInstanceOf(ProductException.class)
                .hasMessage(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());

        // productRepository.findById()는 1번 호출됨
        verify(productRepository, times(1)).findById(invalidProductId);
    }


    // getRelatedProducts 테스트

    @Test
    void getRelatedProducts_성공시_리스트반환() {
        // Given
        Long productId = 1L;
        int limit = 5;

        // Category 생성
        Category category = Category.builder()
                .id(1L)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        // 기준 Product 생성 (이 상품의 연관 상품을 찾음)
        Product baseProduct = Product.of(
                "강아지 사료",
                15000,
                "영양 만점 사료",
                category
        );
        ReflectionTestUtils.setField(baseProduct, "id", productId);

        // 연관 상품들 생성 (같은 카테고리, 리뷰 많은 순)
        Product relatedProduct1 = Product.of(
                "고양이 사료",
                12000,
                "맛있는 사료",
                category
        );
        ReflectionTestUtils.setField(relatedProduct1, "id", 2L);
        ReflectionTestUtils.setField(relatedProduct1, "reviewCount", 100);

        Product relatedProduct2 = Product.of(
                "햄스터 사료",
                8000,
                "건강한 사료",
                category
        );
        ReflectionTestUtils.setField(relatedProduct2, "id", 3L);
        ReflectionTestUtils.setField(relatedProduct2, "reviewCount", 80);

        Pageable pageable = PageRequest.of(0, limit);

        // productRepository.findById() 호출되면 → baseProduct 반환
        given(productRepository.findById(productId))
                .willReturn(Optional.of(baseProduct));

        // productRepository.findByCategoryIdAndIdNotOrderByReviewCountDesc() 호출되면 → 연관 상품 리스트 반환
        given(productRepository.findByCategoryIdAndIdNotOrderByReviewCountDesc(
                category.getId(),
                productId,
                pageable
        )).willReturn(List.of(relatedProduct1, relatedProduct2));

        // When
        List<ProductResponse> results = productQueryService.getRelatedProducts(productId, limit);

        // Then
        assertThat(results).isNotNull(); // 응답이 null이 아님
        assertThat(results).hasSize(2); // 2개의 연관 상품

        // 첫 번째 상품 검증 (리뷰 100개)
        ProductResponse first = results.get(0);
        assertThat(first.getId()).isEqualTo(2L);
        assertThat(first.getName()).isEqualTo("고양이 사료");
        assertThat(first.getReviewCount()).isEqualTo(100);

        // 두 번째 상품 검증 (리뷰 80개)
        ProductResponse second = results.get(1);
        assertThat(second.getId()).isEqualTo(3L);
        assertThat(second.getName()).isEqualTo("햄스터 사료");
        assertThat(second.getReviewCount()).isEqualTo(80);

        // Mock 메서드 호출 검증
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).findByCategoryIdAndIdNotOrderByReviewCountDesc(
                category.getId(),
                productId,
                pageable
        );
    }

    @Test
    void getRelatedProducts_기준상품없으면_예외발생() {
        // Given
        Long invalidProductId = 999L;
        int limit = 5;

        // 기준 상품을 찾을 수 없음
        given(productRepository.findById(invalidProductId))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productQueryService.getRelatedProducts(invalidProductId, limit))
                .isInstanceOf(ProductException.class)
                .hasMessage(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());

        // productRepository.findById()는 1번 호출됨
        verify(productRepository, times(1)).findById(invalidProductId);
        // 예외 발생으로 인해 findByCategoryIdAndIdNotOrderByReviewCountDesc()는 호출 안 됨
        verify(productRepository, times(0)).findByCategoryIdAndIdNotOrderByReviewCountDesc(
                null, null, null
        );
    }

    @Test
    void getRelatedProducts_연관상품이없어도_빈리스트반환() {
        // Given
        Long productId = 1L;
        int limit = 5;

        // Category 생성
        Category category = Category.builder()
                .id(1L)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        // 기준 Product 생성
        Product baseProduct = Product.of(
                "강아지 사료",
                15000,
                "영양 만점 사료",
                category
        );
        ReflectionTestUtils.setField(baseProduct, "id", productId);

        Pageable pageable = PageRequest.of(0, limit);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(baseProduct));

        // 연관 상품이 없음 (빈 리스트 반환)
        given(productRepository.findByCategoryIdAndIdNotOrderByReviewCountDesc(
                category.getId(),
                productId,
                pageable
        )).willReturn(List.of());

        // When
        List<ProductResponse> results = productQueryService.getRelatedProducts(productId, limit);

        // Then
        assertThat(results).isNotNull();     // 응답이 null이 아님
        assertThat(results).isEmpty();       // 빈 리스트

        // Mock 메서드 호출 검증
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).findByCategoryIdAndIdNotOrderByReviewCountDesc(
                category.getId(),
                productId,
                pageable
        );
    }
}