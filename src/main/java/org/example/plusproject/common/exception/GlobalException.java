package org.example.plusproject.common.exception;

import lombok.Getter;
import org.example.plusproject.common.consts.ErrorCode;

@Getter
public class GlobalException extends RuntimeException {

    private final ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
