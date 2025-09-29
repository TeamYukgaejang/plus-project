package org.example.plusproject.domain.category.repository;

import org.example.plusproject.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    // 1. 카테고리 생성 시 이름 중복 여부 확인
    boolean existsByNameAndDeletedAtIsNull(String name);

    // 2. 카테고리 수정 시 자기 자신(id)을 제외한 이름 중복 여부 확인
    boolean existsByNameAndIdNot(String name, Long id);
}