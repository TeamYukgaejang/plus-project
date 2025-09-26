//package org.example.plusproject.domain.search.service.queryservice;
//
//import org.example.plusproject.domain.product.entity.Product;
//import org.example.plusproject.domain.product.repository.ProductRepository;
//import org.example.plusproject.domain.search.dto.response.ProductSearchResponse;
//import org.example.plusproject.domain.search.dto.response.TrendingKeywordResponse;
//import org.example.plusproject.domain.search.entity.SearchKeyword;
//import org.example.plusproject.domain.search.exception.SearchErrorCode;
//import org.example.plusproject.domain.search.exception.SearchException;
//import org.example.plusproject.domain.search.repository.SearchKeywordRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class SearchQueryServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private SearchKeywordRepository searchKeywordRepository;
//
//    @InjectMocks
//    private SearchQueryService searchQueryService;
//
//    @Test
//    @DisplayName("검색어가 비어 있으면 예외 발생")
//    void searchV1_emptyKeyword_throwException() {
//        // when & then
//        assertThatThrownBy(() -> searchQueryService.searchV1("", PageRequest.of(0, 10)))
//                .isInstanceOf(SearchException.class)
//                .hasMessage(SearchErrorCode.SEARCH_KEYWORD_EMPTY.getMessage());
//    }
//
//    @Test
//    @DisplayName("검색 결과가 없으면 예외 발생")
//    void searchV1_noResults_throwException() {
//        // given
//        given(productRepository.findByNameContaining(eq("맥북"), any(PageRequest.class)))
//                .willReturn(Page.empty());
//
//        // when & then
//        assertThatThrownBy(() -> searchQueryService.searchV1("맥북", PageRequest.of(0, 10)))
//                .isInstanceOf(SearchException.class)
//                .hasMessage(SearchErrorCode.SEARCH_RESULT_NOT_FOUND.getMessage());
//    }
//
//    @Test
//    @DisplayName("검색 성공 시 ProductSearchResponse 반환")
//    void searchV1_success() {
//        // given
//        Product product = Product.of("맥북 프로", 3000000, "애플 노트북");
//        Page<Product> productPage = new PageImpl<>(List.of(product));
//
//        given(productRepository.findByNameContaining(eq("맥북"), any(PageRequest.class)))
//                .willReturn(productPage);
//
//        // when
//        Page<ProductSearchResponse> results = searchQueryService.searchV1("맥북", PageRequest.of(0, 10));
//
//        // then
//        assertThat(results).isNotEmpty();
//        assertThat(results.getContent().get(0).getName()).isEqualTo("맥북 프로");
//    }
//
//    @Test
//    @DisplayName("인기 검색어 조회 시 상위 10개 키워드 반환")
//    void getPopularKeywords_success() {
//        // given
//        List<SearchKeyword> keywords = List.of(
//                SearchKeyword.of("맥북"),
//                SearchKeyword.of("아이폰")
//        );
//        keywords.get(0).increaseCount(); // "맥북" → count = 2
//
//        given(searchKeywordRepository.findAll()).willReturn(keywords);
//
//        // when
//        List<TrendingKeywordResponse> results = searchQueryService.getPopularKeywords();
//
//        // then
//        assertThat(results).hasSize(2);
//        assertThat(results.get(0).getKeyword()).isEqualTo("맥북"); // count 높은 순
//    }
//}
