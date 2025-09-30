package org.example.plusproject.domain.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.plusproject.domain.category.entity.Category;

@Getter
@AllArgsConstructor
public class CategorySummaryResponse {
    private Long id;
    private String name;

    public static CategorySummaryResponse from(Category category) {
        return new CategorySummaryResponse(category.getId(), category.getName());
    }
}
