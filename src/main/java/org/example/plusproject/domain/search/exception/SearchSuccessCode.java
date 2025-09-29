package org.example.plusproject.domain.search.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SearchSuccessCode implements SuccessCode {

    SEARCH_SUCCESS(HttpStatus.OK, "검색이 성공적으로 처리되었습니다."),
    SEARCH_TRENDING_SUCCESS(HttpStatus.OK, "인기 검색어 조회가 성공적으로 처리되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}