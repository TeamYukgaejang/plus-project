package org.example.plusproject.domain.search.service.queryservice;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.example.plusproject.domain.search.dto.response.ProductSearchResponse;
import org.example.plusproject.domain.search.dto.response.TrendingKeywordResponse;
import org.example.plusproject.domain.search.exception.SearchErrorCode;
import org.example.plusproject.domain.search.exception.SearchException;
import org.example.plusproject.domain.search.repository.SearchKeywordRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchQueryService {

    private final ProductRepository productRepository;
    private final SearchKeywordRepository searchKeywordRepository;

    // v1 캐시 미적용
    public Page<ProductSearchResponse> searchV1(String keyword, Pageable pageable) {
        validateKeyword(keyword);
        Page<ProductSearchResponse> results = productRepository.findByNameContaining(keyword, pageable)
                .map(ProductSearchResponse::from);

        return results;
    }

    // 인기 검색어
    public List<TrendingKeywordResponse> getPopularKeywords() {
        return searchKeywordRepository.findTop10ByOrderByCountDesc()
                .stream()
                .map(k -> TrendingKeywordResponse.builder()
                        .keyword(k.getKeyword())
                        .count(k.getCount())
                        .build())
                .toList();
    }

    // v2 캐시 적용
    @Cacheable(value = "products", key = "#keyword + '_' + #pageable.pageNumber")
    public Page<ProductSearchResponse> searchV2(String keyword, Pageable pageable) {
        validateKeyword(keyword);
        Page<ProductSearchResponse> results = productRepository.findByNameContaining(keyword, pageable)
                .map(ProductSearchResponse::from);

        if (results.isEmpty()) {
            throw new SearchException(SearchErrorCode.SEARCH_RESULT_NOT_FOUND);
        }

        return results;
    }

    private void validateKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new SearchException(SearchErrorCode.SEARCH_KEYWORD_EMPTY);
        }
    }
}
