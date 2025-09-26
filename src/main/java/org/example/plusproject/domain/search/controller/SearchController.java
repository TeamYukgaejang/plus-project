package org.example.plusproject.domain.search.controller;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.dto.response.PageResponse;
import org.example.plusproject.domain.search.dto.response.ProductSearchResponse;
import org.example.plusproject.domain.search.service.commandservice.SearchCommandService;
import org.example.plusproject.domain.search.service.queryservice.SearchQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchQueryService searchQueryService;
    private final SearchCommandService searchCommandService;

    @GetMapping("/v1/products/search")
    public ResponseEntity<ApiResponse<PageResponse<ProductSearchResponse>>> searchProductsV1(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<ProductSearchResponse> results = searchQueryService.searchV1(keyword, pageable);
        searchCommandService.saveOrIncreaseKeyword(keyword);
        return ApiResponse.pageSuccess(results, "검색 성공 (v1)");
    }
}
