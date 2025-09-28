package org.example.plusproject.common.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    // Auth
    NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "로그인 정보가 일치하지 않습니다."),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),

    // product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),

    // review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 후기입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
