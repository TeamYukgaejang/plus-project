package org.example.plusproject.domain.category.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.plusproject.common.consts.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode implements ErrorCode {

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    CATEGORY_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리명입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
