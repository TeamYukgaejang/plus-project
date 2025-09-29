package org.example.plusproject.domain.category.repository;

import org.example.plusproject.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    // 1. 삭제되지 않은 카테고리 중 이름 중복 여부 확인
    boolean existsByNameAndDeletedAtIsNull(String name);
}
