package org.example.plusproject.domain.category.repository;

import org.example.plusproject.domain.category.entity.Category;
import org.example.plusproject.domain.category.exception.CategoryErrorCode;
import org.example.plusproject.domain.category.exception.CategoryException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    boolean existsByNameAndDeletedAtIsNull(String name);

    Optional<Category> findByIdAndDeletedAtIsNull(Long id);

    List<Category> findAllByDeletedAtIsNull();

    boolean existsByNameAndDeletedAtIsNullAndIdNot(String name, Long id);

    default Category findByIdAndDeletedAtIsNullOrElseThrow(Long id) {
        return findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }
}