package org.example.plusproject.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.plusproject.common.entity.BaseRemovableEntity;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.user.entity.User;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseRemovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int point;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    private Review(String content, int point) {
        this.content = content;
        this.point = point;
    }

    public static Review of(String content, int point) {
        return new Review(content, point);
    }
}
