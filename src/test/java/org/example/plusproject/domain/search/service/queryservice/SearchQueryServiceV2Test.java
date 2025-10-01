package org.example.plusproject.domain.search.service.queryservice;

import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.example.plusproject.domain.search.dto.response.ProductSearchResponse;
import org.example.plusproject.domain.search.exception.SearchErrorCode;
import org.example.plusproject.domain.search.exception.SearchException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class SearchQueryServiceV2Test {

    @Autowired
    private SearchQueryService searchQueryService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void 검색어가_비어있으면_예외발생() {
        assertThatThrownBy(() -> searchQueryService.searchV2(" ", PageRequest.of(0, 10)))
                .isInstanceOf(SearchException.class)
                .hasMessage(SearchErrorCode.SEARCH_KEYWORD_EMPTY.getMessage());
    }

    @Test
    void 검색결과가_없으면_예외발생() {
        assertThatThrownBy(() -> searchQueryService.searchV2("없는상품", PageRequest.of(0, 10)))
                .isInstanceOf(SearchException.class)
                .hasMessage(SearchErrorCode.SEARCH_RESULT_NOT_FOUND.getMessage());
    }

    @Test
    void 검색성공시_결과반환() {
        productRepository.save(Product.of("강아지 사료", 10000, "맛있는 사료", 1L));

        Page<ProductSearchResponse> results =
                searchQueryService.searchV2("사료", PageRequest.of(0, 10));

        assertThat(results).hasSize(1);
        assertThat(results.getContent().get(0).getName()).contains("사료");
    }

    @Test
    void 캐시적용_두번째호출은_DB조회없이_캐시에서가져옴() {
        productRepository.save(Product.of("고양이 사료", 12000, "고양이용 사료", 1L));

        PageRequest pageable = PageRequest.of(0, 10);

        Page<ProductSearchResponse> firstCall =
                searchQueryService.searchV2("사료", pageable);

        String cacheKey = "사료_0_10_UNSORTED";
        Object cachedValue = cacheManager.getCache("products").get(cacheKey, Object.class);
        assertThat(cachedValue).isNotNull();

        Page<ProductSearchResponse> secondCall =
                searchQueryService.searchV2("사료", pageable);

        assertThat(secondCall.getContent()).hasSize(firstCall.getContent().size());
        assertThat(secondCall.getContent().get(0).getName()).isEqualTo("고양이 사료");
    }
}
