package org.example.plusproject.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.plusproject.common.entity.BaseRemovableEntity;
import org.example.plusproject.domain.category.entity.Category;
import org.example.plusproject.domain.review.entity.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends BaseRemovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 1000)
    private String content;

    private int reviewCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    private Product(String name, int price, String content, int reviewCount, Category category) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.reviewCount = reviewCount;
        this.category = category;
    }

    // reviewCount를 0으로 초기화
    public static Product of(String name, int price, String content, Category category) {
        return new Product(name, price, content, 0, category);
    }

    public void update(String name, int price, String content, Category category) {
        this.name = name;
        this.price = price;
        this.content = content;
        this.category = category;
    }
}
