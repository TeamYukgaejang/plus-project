package org.example.plusproject.domain.product.exception;

import org.example.plusproject.common.exception.GlobalException;

public class ProductException extends GlobalException {
    public ProductException(ProductErrorCode productErrorCode) {
        super(productErrorCode);
    }

    public ProductException(ProductErrorCode productErrorCode, ProductSuccessCode productSuccessCode) {
        super(productErrorCode, productSuccessCode);
    }
}
