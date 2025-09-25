package org.example.plusproject.common.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.example.plusproject.common.consts.ErrorCode;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.exception.GlobalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(GlobalException ex) {
        log.error("비즈니스 오류 발생 ", ex);
        return handleExceptionInternal(ex.getErrorCode());
    }

    private ResponseEntity<ApiResponse<Object>> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.error(errorCode));
    }
}
