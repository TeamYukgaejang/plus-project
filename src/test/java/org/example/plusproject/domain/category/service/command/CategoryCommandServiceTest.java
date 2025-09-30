package org.example.plusproject.domain.category.service.command;

import org.example.plusproject.domain.category.dto.request.CategoryCreateRequest;
import org.example.plusproject.domain.category.dto.request.CategoryUpdateRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CategoryCommandServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryCommandServiceImpl categoryCommandService;


    // --- createCategory ---
    @Test
    void createCategory_이름중복시_예외발생() {

        // given
        CategoryCreateRequest request = CategoryCreateRequest.of("사료", "강아지, 고양이, 햄스터 사료");
        given(categoryRepository.existsByNameAndDeletedAtIsNull(request.getName()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryCommandService.createCategory(request))
                .isInstanceOf(CategoryException.class)
                .hasMessage(CategoryErrorCode.CATEGORY_NAME_DUPLICATED.getMessage());

        verify(categoryRepository, times(1))
                .existsByNameAndDeletedAtIsNull(request.getName());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void createCategory_성공시_저장후_응답반환() {

        // given
        CategoryCreateRequest request = CategoryCreateRequest.of("사료", "강아지, 고양이, 햄스터 사료");
        Category savedCategory = Category.of("사료", "강아지, 고양이, 햄스터 사료");

        given(categoryRepository.existsByNameAndDeletedAtIsNull(request.getName()))
                .willReturn(false);
        given(categoryRepository.save(any(Category.class)))
                .willReturn(savedCategory);

        // when
        CategoryResponse response = categoryCommandService.createCategory(request);

        // then
        assertThat(response.getName()).isEqualTo("사료");
        assertThat(response.getDescription()).isEqualTo("강아지, 고양이, 햄스터 사료");

        verify(categoryRepository, times(1))
                .existsByNameAndDeletedAtIsNull(request.getName());
        verify(categoryRepository, times(1))
                .save(any(Category.class));
    }


    // --- updateCategory ---
    @Test
    void updateCategory_이름중복시_예외발생() {

        // given
        Long categoryId = 1L;
        Category category = Category.of("사료", "강아지, 고양이, 햄스터 사료");
        CategoryUpdateRequest request = CategoryUpdateRequest.of("간식", "최신 유행 간식");

        given(categoryRepository.findByIdAndDeletedAtIsNullOrElseThrow(categoryId))
                .willReturn(category);
        given(categoryRepository.existsByNameAndDeletedAtIsNullAndIdNot(request.getName(), categoryId))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryCommandService.updateCategory(categoryId, request))
                .isInstanceOf(CategoryException.class)
                .hasMessage(CategoryErrorCode.CATEGORY_NAME_DUPLICATED.getMessage());

        verify(categoryRepository, times(1))
                .existsByNameAndDeletedAtIsNullAndIdNot(request.getName(), categoryId);
    }

    @Test
    void updateCategory_성공시_수정된정보반환() {

        // given
        Long categoryId = 1L;
        Category category = Category.of("사료", "강아지, 고양이, 햄스터 사료");
        CategoryUpdateRequest request = CategoryUpdateRequest.of("간식", "최신 유행 간식");

        given(categoryRepository.findByIdAndDeletedAtIsNullOrElseThrow(categoryId))
                .willReturn(category);
        given(categoryRepository.existsByNameAndDeletedAtIsNullAndIdNot(request.getName(), categoryId))
                .willReturn(false);

        // when
        CategoryResponse response = categoryCommandService.updateCategory(categoryId, request);

        // then
        assertThat(response.getName()).isEqualTo("간식");
        assertThat(response.getDescription()).isEqualTo("최신 유행 간식");
    }


    // --- deleteCategory ---
    @Test
    void deleteCategory_존재하지않는ID시_예외발생() {

        // given
        Long categoryId = 1L;
        given(categoryRepository.findByIdAndDeletedAtIsNullOrElseThrow(categoryId))
                .willThrow(new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> categoryCommandService.deleteCategory(categoryId))
                .isInstanceOf(CategoryException.class)
                .hasMessage(CategoryErrorCode.CATEGORY_NOT_FOUND.getMessage());

        verify(categoryRepository, times(1))
                .findByIdAndDeletedAtIsNullOrElseThrow(categoryId);
    }

    @Test
    void deleteCategory_성공시_delete호출() {

        // given
        Long categoryId = 1L;
        Category category = Category.of("사료", "강아지, 고양이, 햄스터 사료");
        given(categoryRepository.findByIdAndDeletedAtIsNullOrElseThrow(categoryId))
                .willReturn(category);

        // when
        categoryCommandService.deleteCategory(categoryId);

        // then
        assertThat(category.getDeletedAt()).isNotNull();
        verify(categoryRepository, times(1))
                .findByIdAndDeletedAtIsNullOrElseThrow(categoryId);
    }
}
