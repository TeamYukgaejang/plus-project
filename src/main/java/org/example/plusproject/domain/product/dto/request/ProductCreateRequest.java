package org.example.plusproject.domain.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull
    private Long categoryId; // 실제 Category 엔티티 없이 ID만 저장

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @Min(value = 0)
    private Integer price;

    @NotBlank
    @Size(max = 1000)
    private String content;

    public static ProductCreateRequest of(Long categoryId, String name, Integer price, String content) {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.categoryId = categoryId;
        productCreateRequest.name = name;
        productCreateRequest.price = price;
        productCreateRequest.content = content;
        return productCreateRequest;
    }
}
