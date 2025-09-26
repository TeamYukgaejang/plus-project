package org.example.plusproject.domain.search.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SearchErrorCode implements ErrorCode {

    // 검색 관련 에러코드
    SEARCH_KEYWORD_EMPTY(HttpStatus.BAD_REQUEST, "SEARCH_001", "검색어는 비어 있을 수 없습니다."),
    SEARCH_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH_002", "검색 결과가 존재하지 않습니다."),
    SEARCH_CACHE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SEARCH_003", "캐시 처리 중 오류가 발생했습니다."),
    SEARCH_LOG_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SEARCH_004", "검색 로그 저장에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
