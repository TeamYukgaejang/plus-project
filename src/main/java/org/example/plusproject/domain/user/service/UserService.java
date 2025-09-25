package org.example.plusproject.domain.user.service;

import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.example.plusproject.domain.user.entity.User;

public interface UserService {
    /**
     * 회원가입
     * @param requestDto 회원가입 요청 정보
     * @return 생성된 유저 정보
     */
    User signUp(SignUpRequestDto requestDto);
}
