package org.example.plusproject.domain.category.service.command;

import org.example.plusproject.domain.category.dto.request.CategoryCreateRequest;
import org.example.plusproject.domain.category.dto.request.CategoryUpdateRequest;
import org.example.plusproject.domain.category.dto.response.CategoryResponse;

public interface CategoryCommandService {
    CategoryResponse createCategory(CategoryCreateRequest request);
    CategoryResponse updateCategory(Long id, CategoryUpdateRequest request);
}
