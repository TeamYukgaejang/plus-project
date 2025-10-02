package org.example.plusproject.domain.review.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 후기입니다."),
    REVIEW_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 상품입니다."),
    REVIEW_FORBIDDEN(HttpStatus.FORBIDDEN, "이 후기에 대한 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
