//package org.example.plusproject.domain.search.service.commandservice;
//
//import org.example.plusproject.domain.search.entity.SearchKeyword;
//import org.example.plusproject.domain.search.repository.SearchKeywordRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//import static org.mockito.BDDMockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class SearchCommandServiceTest {
//
//    @Mock
//    private SearchKeywordRepository searchKeywordRepository;
//
//    @InjectMocks
//    private SearchCommandService searchCommandService;
//
//    @Test
//    @DisplayName("기존 검색어가 있으면 count 증가")
//    void saveOrIncreaseKeyword_existingKeyword() {
//        // given
//        SearchKeyword keyword = SearchKeyword.of("맥북");
//        given(searchKeywordRepository.findByKeyword("맥북")).willReturn(Optional.of(keyword));
//
//        // when
//        searchCommandService.saveOrIncreaseKeyword("맥북");
//
//        // then
//        assertThat(keyword.getCount()).isEqualTo(1);
//        verify(searchKeywordRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("검색어가 없으면 새로 저장")
//    void saveOrIncreaseKeyword_newKeyword() {
//        // given
//        given(searchKeywordRepository.findByKeyword("맥북")).willReturn(Optional.empty());
//
//        // when
//        searchCommandService.saveOrIncreaseKeyword("맥북");
//
//        // then
//        verify(searchKeywordRepository, times(1)).save(any(SearchKeyword.class));
//    }
//}
