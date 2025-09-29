package org.example.plusproject.domain.search.controller;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.dto.response.PageResponse;
import org.example.plusproject.domain.search.dto.response.ProductSearchResponse;
import org.example.plusproject.domain.search.dto.response.TrendingKeywordResponse;
import org.example.plusproject.domain.search.exception.SearchSuccessCode;
import org.example.plusproject.domain.search.service.commandservice.SearchCommandService;
import org.example.plusproject.domain.search.service.queryservice.SearchQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchQueryService searchQueryService;
    private final SearchCommandService searchCommandService;

    // v1 검색 조회
    @GetMapping("/v1/search")
    public ApiResponse<PageResponse<ProductSearchResponse>> searchProductsV1(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<ProductSearchResponse> results = searchQueryService.searchV1(keyword, pageable);
        searchCommandService.saveOrIncreaseKeyword(keyword);
        return ApiResponse.of(SearchSuccessCode.SEARCH_SUCCESS, PageResponse.fromPage(results));
    }

    // 인기 검색어 조회
    @GetMapping("/v2/search/trending")
    public ApiResponse<List<TrendingKeywordResponse>> getTrendingKeywords() {
        return ApiResponse.of(
                SearchSuccessCode.SEARCH_TRENDING_SUCCESS,
                searchQueryService.getPopularKeywords()
        );
    }

    // v2 검색 조회
    @GetMapping("/v2/search")
    public ApiResponse<PageResponse<ProductSearchResponse>> searchProductsV2(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<ProductSearchResponse> results = searchQueryService.searchV2(keyword, pageable);
        searchCommandService.saveOrIncreaseKeyword(keyword);
        return ApiResponse.of(SuccessCode.SEARCH_SUCCESS, PageResponse.fromPage(results));
    }

}
