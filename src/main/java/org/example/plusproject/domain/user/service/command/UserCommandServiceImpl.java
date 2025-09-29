package org.example.plusproject.domain.user.service.command;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.jwt.JwtUtil;
import org.example.plusproject.domain.user.dto.request.LoginRequestDto;
import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.example.plusproject.domain.user.dto.response.SignUpResponseDto;
import org.example.plusproject.domain.user.entity.User;
import org.example.plusproject.domain.user.enums.Role;
import org.example.plusproject.domain.user.exception.UserNotFoundException;
import org.example.plusproject.domain.user.exception.UserSuccessCode;
import org.example.plusproject.domain.user.repository.UserRepository;
import org.example.plusproject.domain.user.service.query.UserQueryService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository; // CUD 작업을 위해 유지
    private final UserQueryService userQueryService; // 조회(R) 작업을 위해 추가
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public ApiResponse<SignUpResponseDto> signUp(SignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();

        // 이메일 중복 확인
        if (userQueryService.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 확인
        if (userQueryService.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // User 객체 생성
        User user = User.of(email, encodedPassword, nickname, Role.USER);

        // DB 저장
        User savedUser = userRepository.save(user);

        // DTO로 변환하여 ApiResponse에 담아 반환
        return ApiResponse.of(UserSuccessCode.USER_CREATED, SignUpResponseDto.from(savedUser));
    }

    @Override
    public String login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user;
        try {
            user = userQueryService.findUserByEmail(email);
        } catch (UserNotFoundException e) {
            // 사용자 존재 여부가 노출되지 않도록, 기존과 동일한 예외를 던집니다.
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 반환
        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userQueryService.findUserById(userId);
        user.delete();
    }
}