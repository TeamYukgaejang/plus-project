package org.example.plusproject.domain.category.exception;

import org.example.plusproject.common.consts.ErrorCode;
import org.example.plusproject.common.exception.GlobalException;

public class CategoryException extends GlobalException {
    public CategoryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
