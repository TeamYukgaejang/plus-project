package org.example.plusproject.domain.user.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
}
