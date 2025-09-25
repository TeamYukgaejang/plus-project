package org.example.plusproject.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.plusproject.common.entity.BaseEntity;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private String content;

    private int reviewCount;

    @Builder
    private Product(String name, int price, String content, int reviewCount) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.reviewCount = reviewCount;
    }

    public static Product of(String name, int price, String content) {
        return new Product(name, price, content, 0);
    }
}
