package org.example.plusproject.domain.product.service.command;

import org.example.plusproject.domain.category.entity.Category;
import org.example.plusproject.domain.category.exception.CategoryErrorCode;
import org.example.plusproject.domain.category.exception.CategoryException;
import org.example.plusproject.domain.category.repository.CategoryRepository;
import org.example.plusproject.domain.product.dto.request.ProductRequest;
import org.example.plusproject.domain.product.dto.response.ProductResponse;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.exception.ProductErrorCode;
import org.example.plusproject.domain.product.exception.ProductException;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCommandServiceImplTest {

    // 가짜 ProductRepository 생성
    @Mock
    private ProductRepository productRepository;

    // 가짜 CategoryRepository 생성
    @Mock
    private CategoryRepository categoryRepository;

    // 테스트 대상: 진짜 객체 (Mock들을 주입받음)
    private ProductCommandServiceImpl productCommandService;

    // 각 테스트 실행 전에 매번 실행됨
    @BeforeEach
    void setUp() {
        // 진짜 ProductCommandServiceImpl 생성 (Mock 주입)
        productCommandService = new ProductCommandServiceImpl(
                productRepository,
                categoryRepository
        );
    }

    @Test
    @DisplayName("상품 등록 성공")
    void 상품_등록_성공() {
        // Given
        Long categoryId = 1L;
        String productName = "강아지 사료";
        Integer price = 10000;
        String content = "맛있는 사료";

        // 요청 DTO 생성
        ProductRequest request = ProductRequest.of(categoryId, productName, price, content);

        // 가짜 Category 객체 생성
        Category category = Category.builder()
                .id(categoryId)
                .name("반려동물")
                .description("반려동물 용품")
                .build();

        // 가짜 Product 객체 생성
        Product product = Product.of(productName, price, content, category);
        ReflectionTestUtils.setField(product, "id", 1L); // ID 강제 설정

        // categoryRepository.findById()가 호출되면 category 반환
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        // productRepository.save()가 호출되면 product 반환
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        // When
        ProductResponse response = productCommandService.createProduct(request);

        // Then
        assertThat(response).isNotNull();                           // 응답이 null이 아님
        assertThat(response.getId()).isEqualTo(1L);        // ID가 1L
        assertThat(response.getName()).isEqualTo(productName);      // 이름이 일치
        assertThat(response.getPrice()).isEqualTo(price);           // 가격이 일치
        assertThat(response.getContent()).isEqualTo(content);       // 내용이 일치
        assertThat(response.getCategoryId()).isEqualTo(categoryId); // 카테고리 ID 일치

        // Mock 메서드가 실제로 호출되었는지 검증
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 등록 실패 - 카테고리 없음")
    void 상품_생성_실패_카테고리없음() {
        // Given
        Long invalidCategoryId = 999L;
        ProductRequest request = ProductRequest.of(
                invalidCategoryId,
                "상품명",
                10000,
                "설명"
        );

        // categoryRepository.findById()가 호출되면 빈 Optional 반환
        when(categoryRepository.findById(invalidCategoryId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productCommandService.createProduct(request))
                .isInstanceOf(CategoryException.class) // CategoryException이 발생
                .hasMessage(CategoryErrorCode.CATEGORY_NOT_FOUND.getMessage()); // 메시지 확인

        // categoryRepository.findById()는 호출됨
        verify(categoryRepository, times(1)).findById(invalidCategoryId);
        // productRepository.save()는 호출되지 않음 (예외 발생으로 중단)
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("상품 정보 수정 성공")
    void 상품_정보_수정_성공() {
        // Given
        Long productId = 1L;
        Long categoryId = 1L;
        String updatedName = "수정된 상품명";
        Integer updatedPrice = 20000;
        String updatedContent = "수정된 설명";

        ProductRequest request = ProductRequest.of(
                categoryId,
                updatedName,
                updatedPrice,
                updatedContent
        );

        // 기존 Product 객체
        Category category = Category.builder()
                .id(categoryId)
                .name("카테고리")
                .description("설명")
                .build();

        Product existingProduct = Product.of("원래 상품명", 10000, "원래 설명", category);
        ReflectionTestUtils.setField(existingProduct, "id", productId);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        // When
        ProductResponse response = productCommandService.updateProduct(productId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(productId);
        assertThat(response.getName()).isEqualTo(updatedName); // 이름이 변경됨
        assertThat(response.getPrice()).isEqualTo(updatedPrice); // 가격이 변경됨
        assertThat(response.getContent()).isEqualTo(updatedContent); // 내용이 변경됨

        // update 메서드가 실제로 호출되었는지 확인
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("상품 정보 수정 실패 - 상품 없음")
    void 상품_정보_수정_실패_상품없음() {
        // Given
        Long invalidProductId = 999L;
        ProductRequest request = ProductRequest.of(1L, "상품명", 10000, "설명");

        // 상품을 찾을 수 없음
        when(productRepository.findById(invalidProductId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productCommandService.updateProduct(invalidProductId, request))
                .isInstanceOf(ProductException.class)
                .hasMessage(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());

        verify(productRepository, times(1)).findById(invalidProductId);
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void 상품_삭제_성공() {
        // Given
        Long productId = 1L;

        Category category = Category.builder()
                .id(1L)
                .name("카테고리")
                .description("설명")
                .build();

        Product product = Product.of("상품명", 10000, "설명", category);
        ReflectionTestUtils.setField(product, "id", productId);

        when(productRepository.findByIdIncludingDeleted(productId))
                .thenReturn(Optional.of(product));

        // When
        ProductResponse response = productCommandService.deleteProduct(productId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(productId);
        assertThat(response.getDeletedAt()).isNotNull(); // deletedAt이 설정됨

        verify(productRepository, times(1)).findByIdIncludingDeleted(productId);
    }

    @Test
    @DisplayName("상품 삭제 실패 - 이미 삭제된 상품")
    void 상품_삭제_실패_이미삭제됨() {
        // Given
        Long productId = 1L;

        Category category = Category.builder()
                .id(1L)
                .name("카테고리")
                .description("설명")
                .build();

        Product product = Product.of("상품명", 10000, "설명", category);
        ReflectionTestUtils.setField(product, "id", productId);
        product.delete(); // 이미 삭제됨

        when(productRepository.findByIdIncludingDeleted(productId))
                .thenReturn(Optional.of(product));

        // When & Then
        assertThatThrownBy(() -> productCommandService.deleteProduct(productId))
                .isInstanceOf(ProductException.class)
                .hasMessage(ProductErrorCode.PRODUCT_ALREADY_DELETED.getMessage());

        verify(productRepository, times(1)).findByIdIncludingDeleted(productId);
    }

    @Test
    @DisplayName("상품 삭제 실패 - 상품 없음")
    void 상품_삭제_실패_상품없음() {
        // Given
        Long invalidProductId = 999L;

        when(productRepository.findByIdIncludingDeleted(invalidProductId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productCommandService.deleteProduct(invalidProductId))
                .isInstanceOf(ProductException.class)
                .hasMessage(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());

        verify(productRepository, times(1)).findByIdIncludingDeleted(invalidProductId);
    }
}