package org.example.plusproject.domain.search.service.queryservice;

import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.product.repository.ProductRepository;
import org.example.plusproject.domain.search.dto.response.ProductSearchResponse;
import org.example.plusproject.domain.search.dto.response.TrendingKeywordResponse;
import org.example.plusproject.domain.search.entity.SearchKeyword;
import org.example.plusproject.domain.search.exception.SearchErrorCode;
import org.example.plusproject.domain.search.exception.SearchException;
import org.example.plusproject.domain.search.repository.SearchKeywordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SearchQueryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SearchKeywordRepository searchKeywordRepository;

    @InjectMocks
    private SearchQueryService searchQueryService;

    @Test
    void 검색어가_비어있으면_예외발생() {
        String keyword = " ";

        assertThatThrownBy(() -> searchQueryService.searchV1(keyword, PageRequest.of(0, 10)))
                .isInstanceOf(SearchException.class)
                .hasMessage(SearchErrorCode.SEARCH_KEYWORD_EMPTY.getMessage());
    }

    @Test
    void 상품명이_포함된_검색결과_반환() {
        String keyword = "사료";
        PageRequest pageable = PageRequest.of(0, 10);
        Product product = Product.of("고양이 사료", 10000, "맛있는 사료", 1L);
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);

        given(productRepository.findByNameContaining(keyword, pageable)).willReturn(page);

        Page<ProductSearchResponse> results = searchQueryService.searchV1(keyword, pageable);

        assertThat(results).hasSize(1);
        assertThat(results.getContent().get(0).getName()).contains("사료");
    }

    @Test
    void 인기검색어_Top10_반환() {
        SearchKeyword keyword1 = SearchKeyword.of("사료");
        keyword1.increaseCount();
        SearchKeyword keyword2 = SearchKeyword.of("강아지 사료");
        keyword2.increaseCount();
        keyword2.increaseCount();

        given(searchKeywordRepository.findTop10ByOrderByCountDesc())
                .willReturn(List.of(keyword2, keyword1));

        List<TrendingKeywordResponse> results = searchQueryService.getPopularKeywords();

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getKeyword()).isEqualTo("강아지 사료");
        assertThat(results.get(0).getCount()).isEqualTo(2);
    }
}
