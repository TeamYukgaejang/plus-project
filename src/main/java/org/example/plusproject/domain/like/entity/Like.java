package org.example.plusproject.domain.like.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.plusproject.common.entity.BaseEntity;
import org.example.plusproject.domain.review.entity.Review;
import org.example.plusproject.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "review_id"})
)
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    private Like(Review review, User user) {
        this.review = review;
        this.user = user;
    }

    public static Like of(Review review, User user) {
        return new Like(review, user);
    }
}
