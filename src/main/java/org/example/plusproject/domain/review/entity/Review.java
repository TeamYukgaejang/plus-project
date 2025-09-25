package org.example.plusproject.domain.review.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.example.plusproject.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int point;

    @Builder
    private Review(String content, int point) {
        this.content = content;
        this.point = point;
    }

    public static Review of(String content, int point) {
        return new Review(content, point);
    }
}
