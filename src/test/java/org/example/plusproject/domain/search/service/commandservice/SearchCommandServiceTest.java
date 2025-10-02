package org.example.plusproject.domain.search.service.commandservice;

import org.example.plusproject.domain.search.entity.SearchKeyword;
import org.example.plusproject.domain.search.repository.SearchKeywordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SearchCommandServiceTest {

    @Mock
    private SearchKeywordRepository searchKeywordRepository;

    @InjectMocks
    private SearchCommandService searchCommandService;

    @Test
    void 기존검색어가_있으면_count증가() {
        SearchKeyword keyword = SearchKeyword.of("사료");
        given(searchKeywordRepository.findByKeywordForUpdate("사료"))
                .willReturn(Optional.of(keyword));

        searchCommandService.saveOrIncreaseKeyword("사료");

        assertThat(keyword.getCount()).isEqualTo(2);
        verify(searchKeywordRepository, never()).save(any());
    }


    @Test
    void 검색어가_없으면_새로저장() {
        given(searchKeywordRepository.findByKeywordForUpdate("사료"))
                .willReturn(Optional.empty());

        searchCommandService.saveOrIncreaseKeyword("사료");

        verify(searchKeywordRepository, times(1))
                .save(argThat(saved ->
                        saved.getKeyword().equals("사료") && saved.getCount() == 1
                ));
    }
}