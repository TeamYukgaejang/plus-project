package org.example.plusproject.domain.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.category.dto.request.CategoryCreateRequest;
import org.example.plusproject.domain.category.dto.response.CategoryResponse;
import org.example.plusproject.domain.category.exception.CategorySuccessCode;
import org.example.plusproject.domain.category.service.command.CategoryCommandService;
import org.example.plusproject.domain.category.service.query.CategoryQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> response = categoryQueryService.getAllCategories();

        return ResponseEntity
                .status(CategorySuccessCode.CATEGORIES_FETCHED.getHttpStatus())
                .body(ApiResponse.of(CategorySuccessCode.CATEGORIES_FETCHED, response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse response = categoryQueryService.getCategoryById(id);

        return ResponseEntity
                .status(CategorySuccessCode.CATEGORY_FETCHED.getHttpStatus())
                .body(ApiResponse.of(CategorySuccessCode.CATEGORY_FETCHED, response));
    }
}
