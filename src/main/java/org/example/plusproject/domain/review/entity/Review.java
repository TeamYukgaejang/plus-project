package org.example.plusproject.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.plusproject.common.entity.BaseRemovableEntity;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"})
) // 유니크 제약조건 - 해당 조합과 같은 조합의 레코드는 등록 불가
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
    private Review(String content, int point, User user, Product product) {
        this.content = content;
        this.point = point;
        this.user = user;
        this.product = product;
    }

    public static Review of(String content, int point, User user, Product product) {
        return new Review(
                content,
                point,
                user,
                product);
    }

    public void update(String content, int point) {
        this.content = content;
        this.point = point;
    }

    public void incrLike() {
        this.likeCount++;
    }

    public void decrLike() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
