package org.example.plusproject.domain.user.service.command;

import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
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

        // DTO로 변환하여 반환
        return SignUpResponseDto.from(savedUser);
    }
}
