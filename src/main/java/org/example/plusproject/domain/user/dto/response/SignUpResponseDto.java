package org.example.plusproject.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.plusproject.domain.user.entity.User;

@Getter
public class SignUpResponseDto {

    private final String email;
    private final String nickname;

    @Builder
    private SignUpResponseDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public static SignUpResponseDto from(User user) {
        return SignUpResponseDto.builder()
            .email(user.getEmail())
            .nickname(user.getNickname())
            .build();
    }
}
