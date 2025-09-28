package org.example.plusproject.domain.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.category.dto.request.CategoryCreateRequest;
import org.example.plusproject.domain.category.dto.response.CategoryResponse;
import org.example.plusproject.domain.category.service.command.CategoryCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryCommandService categoryCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request
    ) {
        CategoryResponse response = categoryCommandService.createCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(SuccessCode.REQUEST_SUCCESS, response));
    }
}
