package org.example.plusproject.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.plusproject.common.entity.BaseRemovableEntity;


@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends BaseRemovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long categoryId;
    // 연관관계 설정 예정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category category;


    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 1000)
    private String content;

    private int reviewCount;

    @Builder
    private Product(String name, int price, String content, int reviewCount, long categoryId) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.reviewCount = reviewCount;
        this.categoryId = categoryId;
    }

    // reviewCount를 0으로 초기화
    public static Product of(String name, int price, String content, long categoryId) {
        return new Product(name, price, content, 0, categoryId);
    }

    public void update(String name, int price, String content, Long categoryId) {
        this.name = name;
        this.price = price;
        this.content = content;
        this.categoryId = categoryId;
    }
}
