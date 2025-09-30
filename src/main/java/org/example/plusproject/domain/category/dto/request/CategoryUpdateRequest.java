package org.example.plusproject.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String description;

    private CategoryUpdateRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static CategoryUpdateRequest of(String name, String description) {
        return new CategoryUpdateRequest(name, description);
    }
}
