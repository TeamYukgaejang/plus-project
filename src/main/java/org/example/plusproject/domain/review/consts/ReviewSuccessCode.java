package org.example.plusproject.domain.review.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewSuccessCode implements SuccessCode {

    REVIEW_GET_SUCCESS(HttpStatus.OK, "후기가 성공적으로 조회되었습니다."),
    REVIEW_CREATED(HttpStatus.CREATED, "후기가 성공적으로 등록되었습니다."),
    REVIEW_DELETED(HttpStatus.OK, "후기가 성공적으로 삭제되었습니다."),
    REVIEW_UPDATED(HttpStatus.OK, "후기가 성공적으로 수정되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
