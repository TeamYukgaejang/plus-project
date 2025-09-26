package org.example.plusproject.domain.user.service.command;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.consts.SuccessCode;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.jwt.JwtUtil;
import org.example.plusproject.domain.user.dto.request.LoginRequestDto;
import org.example.plusproject.domain.user.dto.response.SignUpResponseDto;
import org.example.plusproject.domain.user.entity.User;
import org.example.plusproject.domain.user.repository.UserRepository;
import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.plusproject.domain.user.enums.Role;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public ApiResponse<SignUpResponseDto> signUp(SignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        String password = requestDto.getPassword();

        // 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 확인
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // User 객체 생성
        User user = User.of(email, encodedPassword, nickname, Role.USER);

        // DB 저장
        User savedUser = userRepository.save(user);

        // DTO로 변환하여 ApiResponse에 담아 반환
        return ApiResponse.of(SuccessCode.USER_CREATED, SignUpResponseDto.from(savedUser));
    }

    @Override
    public String login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 반환
        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.")
        );
        userRepository.delete(user);
    }
}
