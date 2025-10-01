package org.example.plusproject.domain.like.consts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode implements ErrorCode {

    LIKE_OWN_REVIEW_FORBIDDEN(HttpStatus.BAD_REQUEST, "자신의 후기에는 좋아요를 남길 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
