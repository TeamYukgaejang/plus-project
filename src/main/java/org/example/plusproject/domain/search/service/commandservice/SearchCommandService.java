package org.example.plusproject.domain.search.service.commandservice;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.search.entity.SearchKeyword;
import org.example.plusproject.domain.search.repository.SearchKeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchCommandService {

    private final SearchKeywordRepository searchKeywordRepository;

    // 검색 시 검색어 count 증가
    public void saveOrIncreaseKeyword(String keyword) {
        searchKeywordRepository.findByKeywordForUpdate(keyword)
                .ifPresentOrElse(
                        SearchKeyword::increaseCount,
                        () -> searchKeywordRepository.save(SearchKeyword.of(keyword))
                );
    }
}
