package org.example.plusproject.common.consts;


import org.springframework.http.HttpStatus;

public interface SuccessCode {

    HttpStatus getHttpStatus();

    String getMessage();
}

//    // search
//    SEARCH_SUCCESS(HttpStatus.OK, "검색이 성공적으로 처리되었습니다."),
//    SEARCH_TRENDING_SUCCESS(HttpStatus.OK, "인기 검색어 조회가 성공적으로 처리되었습니다.");

