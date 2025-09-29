package org.example.plusproject.common.consts;


import org.springframework.http.HttpStatus;

public interface SuccessCode {

    HttpStatus getHttpStatus();

    String getMessage();
}