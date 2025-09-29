package org.example.plusproject.domain.category.service.query;

import org.example.plusproject.domain.category.dto.response.CategoryResponse;
import org.example.plusproject.domain.category.dto.response.CategorySummaryResponse;

import java.util.List;

public interface CategoryQueryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategorySummaryResponse findById(Long id);
}
