package org.example.plusproject.domain.search.repository;

import jakarta.persistence.LockModeType;
import org.example.plusproject.domain.search.entity.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
    Optional<SearchKeyword> findByKeyword(String keyword);
    List<SearchKeyword> findTop10ByOrderByCountDesc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select k from SearchKeyword k where k.keyword = :keyword")
    Optional<SearchKeyword> findByKeywordForUpdate(@Param("keyword") String keyword);
}
