package org.example.plusproject.domain.category.service.command;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.category.dto.request.CategoryCreateRequest;
import org.example.plusproject.domain.category.dto.response.CategoryResponse;
import org.example.plusproject.domain.category.entity.Category;
import org.example.plusproject.domain.category.exception.CategoryErrorCode;
import org.example.plusproject.domain.category.exception.CategoryException;
import org.example.plusproject.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {

        if (categoryRepository.existsByNameAndDeletedAtIsNull(request.getName())) {
            throw new CategoryException(CategoryErrorCode.CATEGORY_NAME_DUPLICATED);
        }

        Category savedCategory = categoryRepository.save(
                Category.of(request.getName(), request.getDescription())
        );

        return CategoryResponse.from(savedCategory);
    }
}