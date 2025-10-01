package org.example.plusproject.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.product.entity.Product;
import org.example.plusproject.domain.review.entity.Review;
import org.example.plusproject.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class ReviewSaveResponse {

    private final Long id;
    private final int point;
    private final String context;
    private final Long productId;
    private final String productName;
    private final Long userId;
    private final String nickname;
    private final Long reviewId;
    private final LocalDateTime createdAt;

    public static ReviewSaveResponse of(Review review, Product product, User user) {
        return  ReviewSaveResponse.builder()
                .id(review.getId())
                .point(review.getPoint())
                .context(review.getContent())
                .productId(product.getId())
                .productName(product.getName())
                .userId(user.getId())
                .nickname(user.getNickname())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
