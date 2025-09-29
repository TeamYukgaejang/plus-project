package org.example.plusproject.domain.category.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.plusproject.common.consts.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CategorySuccessCode implements SuccessCode {

    CATEGORY_CREATED(HttpStatus.CREATED, "카테고리가 생성되었습니다."),
    CATEGORY_FETCHED(HttpStatus.OK, "카테고리가 조회되었습니다."),
    CATEGORIES_FETCHED(HttpStatus.OK, "카테고리 목록이 조회되었습니다."),
    CATEGORY_UPDATED(HttpStatus.OK, "카테고리가 수정되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
