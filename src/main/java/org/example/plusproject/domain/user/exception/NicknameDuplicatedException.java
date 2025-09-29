package org.example.plusproject.domain.user.exception;

public class NicknameDuplicatedException extends UserException {
    public NicknameDuplicatedException() {
        super(UserErrorCode.NICKNAME_DUPLICATED);
    }
}
