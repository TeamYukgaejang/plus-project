package org.example.plusproject.common.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    REQUEST_SUCCESS(HttpStatus.OK, "요청이 성공적으로 처리되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
