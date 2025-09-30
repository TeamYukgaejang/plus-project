package org.example.plusproject.domain.category.service.query;

import org.example.plusproject.domain.category.dto.response.CategoryResponse;
import org.example.plusproject.domain.category.entity.Category;
import org.example.plusproject.domain.category.exception.CategoryErrorCode;
import org.example.plusproject.domain.category.exception.CategoryException;
import org.example.plusproject.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CategoryQueryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryQueryServiceImpl categoryQueryService;

    // --- getAllCategories ---
    @Test
    void getAllCategories_성공시_리스트반환() {

        // given
        Category category1 = Category.of("사료", "강아지, 고양이, 햄스터 사료");
        Category category2 = Category.of("간식", "최신 유행 간식");
        given(categoryRepository.findAllByDeletedAtIsNull())
                .willReturn(List.of(category1, category2));

        // when
        List<CategoryResponse> results = categoryQueryService.getAllCategories();

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getName()).isEqualTo("사료");
        assertThat(results.get(1).getName()).isEqualTo("간식");
    }


    // --- getCategoryById ---
    @Test
    void getCategoryById_존재하지않으면_예외발생() {

        // given
        Long categoryId = 1L;
        given(categoryRepository.findByIdAndDeletedAtIsNullOrElseThrow(categoryId))
                .willThrow(new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> categoryQueryService.getCategoryById(categoryId))
                .isInstanceOf(CategoryException.class)
                .hasMessage(CategoryErrorCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    void getCategoryById_성공시_응답반환() {

        // given
        Long categoryId = 1L;
        Category category = Category.of("사료", "강아지, 고양이, 햄스터 사료");
        given(categoryRepository.findByIdAndDeletedAtIsNullOrElseThrow(categoryId))
                .willReturn(category);

        // when
        CategoryResponse response = categoryQueryService.getCategoryById(categoryId);

        // then
        assertThat(response.getName()).isEqualTo("사료");
        assertThat(response.getDescription()).isEqualTo("강아지, 고양이, 햄스터 사료");
    }
}

