package org.example.plusproject.domain.user.service.command;

import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.example.plusproject.domain.user.dto.response.SignUpResponseDto;

public interface UserCommandService {
    /**
     * 회원가입
     * @param requestDto 회원가입 요청 정보
     * @return 생성된 유저 정보 DTO
     */
    SignUpResponseDto signUp(SignUpRequestDto requestDto);
}
