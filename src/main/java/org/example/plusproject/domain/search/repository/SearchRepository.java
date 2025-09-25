package org.example.plusproject.domain.search.repository;

import org.example.plusproject.domain.search.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long> {
}
