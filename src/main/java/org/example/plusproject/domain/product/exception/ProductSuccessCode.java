package org.example.plusproject.domain.product.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements SuccessCode {

    PRODUCT_CREATED(HttpStatus.CREATED, "상품이 성공적으로 등록되었습니다."),
    PRODUCT_GET_SUCCESS(HttpStatus.OK, "상품이 성공적으로 조회되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
