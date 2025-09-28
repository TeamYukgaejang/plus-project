package org.example.plusproject.domain.category.service.query;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.category.dto.response.CategoryResponse;
import org.example.plusproject.domain.category.entity.Category;
import org.example.plusproject.domain.category.exception.CategoryErrorCode;
import org.example.plusproject.domain.category.exception.CategoryException;
import org.example.plusproject.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        return CategoryResponse.from(category);
    }
}
