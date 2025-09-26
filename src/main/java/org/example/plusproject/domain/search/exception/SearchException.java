package org.example.plusproject.domain.search.exception;

import org.example.plusproject.common.exception.GlobalException;

public class SearchException extends GlobalException {
    public SearchException(SearchErrorCode errorCode) {
        super(errorCode);
    }
}
