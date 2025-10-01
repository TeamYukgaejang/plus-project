package org.example.plusproject.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.domain.review.entity.Review;
import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class ReviewResponse {

    private final Long id;
    private final int point;
    private final String context;
    private final Long productId;
    private final String productName;
    private final Long userId;
    private final String nickname;
    private final Long likes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewResponse of(Review review, Long likeCount) {
        return ReviewResponse.builder()
                .id(review.getId())
                .point(review.getPoint())
                .context(review.getContent())
                .productId(review.getProduct().getId())
                .productName(review.getProduct().getName())
                .userId(review.getUser().getId())
                .nickname(review.getUser().getNickname())
                .likes(likeCount)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
