package org.example.plusproject.domain.category.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.category.dto.request.CategoryCreateRequest;
import org.example.plusproject.domain.category.dto.request.CategoryUpdateRequest;
import org.example.plusproject.domain.category.dto.response.CategoryResponse;
import org.example.plusproject.domain.category.exception.CategorySuccessCode;
import org.example.plusproject.domain.category.service.command.CategoryCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminCategoryController {

    private final CategoryCommandService categoryCommandService;


    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request
    ) {
        CategoryResponse response = categoryCommandService.createCategory(request);

        return ResponseEntity
                .status(CategorySuccessCode.CATEGORY_CREATED.getHttpStatus())
                .body(ApiResponse.of(CategorySuccessCode.CATEGORY_CREATED, response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request
    ) {
        CategoryResponse response = categoryCommandService.updateCategory(id, request);

        return ResponseEntity
                .status(CategorySuccessCode.CATEGORY_UPDATED.getHttpStatus())
                .body(ApiResponse.of(CategorySuccessCode.CATEGORY_UPDATED, response));
    }
}
