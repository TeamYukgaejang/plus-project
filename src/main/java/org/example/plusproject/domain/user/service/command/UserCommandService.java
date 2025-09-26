package org.example.plusproject.domain.user.service.command;

import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.user.dto.request.LoginRequestDto;
import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.example.plusproject.domain.user.dto.response.SignUpResponseDto;

public interface UserCommandService {
    /**
     * 회원가입
     * @param requestDto 회원가입 요청 정보
     * @return 생성된 유저 정보 DTO를 담은 ApiResponse
     */
    ApiResponse<SignUpResponseDto> signUp(SignUpRequestDto requestDto);

    /**
     * 로그인
     * @param requestDto 로그인 요청 정보
     * @return 생성된 JWT
     */
    String login(LoginRequestDto requestDto);

    /**
     * 회원 탈퇴
     * @param userId 탈퇴할 사용자의 ID
     */
    void deleteUser(Long userId);
}
