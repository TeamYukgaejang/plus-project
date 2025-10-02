package org.example.plusproject.domain.user.exception;

public class EmailDuplicatedException extends UserException {
    public EmailDuplicatedException() {
        super(UserErrorCode.EMAIL_DUPLICATED);
    }
}
