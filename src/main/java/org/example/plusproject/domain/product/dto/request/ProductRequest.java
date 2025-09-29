package org.example.plusproject.domain.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotNull
    private Long categoryId; // 실제 Category 엔티티 없이 ID만 저장

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @Min(value = 1) // 상품 가격은 0보다 커야 함
    private Integer price;

    @NotBlank
    @Size(max = 1000)
    private String content;

    public static ProductRequest of(Long categoryId, String name, Integer price, String content) {
        return new ProductRequest(categoryId, name, price, content);
    }
}
