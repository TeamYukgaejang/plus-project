package org.example.plusproject.common.dto.response;

import lombok.Getter;
import org.example.plusproject.common.consts.ErrorCode;
import org.example.plusproject.common.consts.SuccessCode;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> of(SuccessCode code, T data) {
        return new ApiResponse<>(
                true,
                code.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> of(Error code, T data) {
        return new ApiResponse<>(
                false,
                code.getMessage(),
                data
        );
    }
}