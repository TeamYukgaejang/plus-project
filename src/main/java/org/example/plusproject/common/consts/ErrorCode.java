package org.example.plusproject.common.consts;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    String getMessage();
}

// 병합 과정에서 필요하실 것 같은 부분 주석 처리해서 넣어뒀습니다.

// User
//USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),

// Auth
//NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
//EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
//LOGIN_FAILED(HttpStatus.BAD_REQUEST, "로그인 정보가 일치하지 않습니다."),
//FORBIDDEN_USER(HttpStatus.FORBIDDEN, "권한이 없습니다."),

// product
//PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
//PRODUCT_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 상품입니다."),
//INVALID_PRODUCT_CONTENT(HttpStatus.BAD_REQUEST, "상품 설명은 필수입니다."),
//INVALID_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, "상품 가격은 0보다 커야 합니다."),

// review
//REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 후기입니다.");
