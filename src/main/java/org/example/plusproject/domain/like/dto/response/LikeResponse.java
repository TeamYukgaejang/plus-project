package org.example.plusproject.domain.like.dto.response;

import lombok.*;
import org.example.plusproject.domain.like.entity.Like;

@Getter
@RequiredArgsConstructor
@Builder
public class LikeResponse {

    private final Long userId;
    private final String nickname;
    private final Long reviewId;

    public static LikeResponse of(Like like) {
        return LikeResponse.builder()
                .userId(like.getUser().getId())
                .nickname(like.getUser().getNickname())
                .reviewId(like.getReview().getId())
                .build();
    }
}
