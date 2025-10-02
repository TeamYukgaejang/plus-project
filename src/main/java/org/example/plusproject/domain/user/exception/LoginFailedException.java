package org.example.plusproject.domain.user.exception;

public class LoginFailedException extends UserException {
    public LoginFailedException() {
        super(UserErrorCode.LOGIN_FAILED);
    }
}
