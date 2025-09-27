package org.example.plusproject.common.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    REQUEST_SUCCESS(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
    USER_CREATED(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다."),

    // product
    PRODUCT_CREATED(HttpStatus.CREATED, "상품이 성공적으로 등록되었습니다."),

    // search
    SEARCH_SUCCESS(HttpStatus.OK, "검색이 성공적으로 처리되었습니다."),
    SEARCH_TRENDING_SUCCESS(HttpStatus.OK, "인기 검색어 조회가 성공적으로 처리되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
