package org.example.plusproject.domain.search.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.plusproject.domain.product.entity.Product;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchResponse {
    private Long id;
    private String name;
    private int price;
    private String content;
    private int reviewCount;

    public static ProductSearchResponse from(Product product) {
        return ProductSearchResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .content(product.getContent())
                .reviewCount(product.getReviewCount())
                .build();
    }
}
