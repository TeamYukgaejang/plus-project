package org.example.plusproject.domain.product.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    PRODUCT_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 상품입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
