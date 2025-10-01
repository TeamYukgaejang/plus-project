package org.example.plusproject.domain.like.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeSuccessCode implements SuccessCode {

    LIKE_SUCCESS(HttpStatus.OK, "해당 후기에 성공적으로 좋아요를 남겼습니다."),
    LIKE_REMOVED(HttpStatus.OK, "좋아요가 성공적으로 취소되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
