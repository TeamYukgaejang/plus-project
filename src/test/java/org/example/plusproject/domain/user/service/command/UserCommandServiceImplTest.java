package org.example.plusproject.domain.user.service.command;

import org.example.plusproject.common.jwt.JwtUtil;
import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.example.plusproject.domain.user.entity.User;
import org.example.plusproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() {
        // 준비
        SignUpRequestDto requestDto = new SignUpRequestDto("test@test.com", "password123!", "nickname");
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(requestDto.getNickname())).thenReturn(false);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(User.builder().build());

        // 실행
        userCommandService.signUp(requestDto);

        // 검증
        verify(userRepository).existsByEmail(requestDto.getEmail());
        verify(userRepository).existsByNickname(requestDto.getNickname());
        verify(passwordEncoder).encode(requestDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void 회원가입_실패_이메일중복() {
        // 준비
        SignUpRequestDto requestDto = new SignUpRequestDto("test@test.com", "password123!", "nickname");
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        // 실행 & 검증
        assertThrows(IllegalArgumentException.class, () -> {
            userCommandService.signUp(requestDto);
        });

        verify(userRepository).existsByEmail(requestDto.getEmail());
        verify(userRepository, never()).existsByNickname(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void 회원가입_실패_닉네임중복() {
        // 준비
        SignUpRequestDto requestDto = new SignUpRequestDto("test@test.com", "password123!", "nickname");
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(requestDto.getNickname())).thenReturn(true);

        // 실행 & 검증
        assertThrows(IllegalArgumentException.class, () -> {
            userCommandService.signUp(requestDto);
        });

        verify(userRepository).existsByEmail(requestDto.getEmail());
        verify(userRepository).existsByNickname(requestDto.getNickname());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("로그인 성공")
    void 로그인_성공() {
        // 준비
        LoginRequestDto requestDto = new LoginRequestDto("test@test.com", "password123!");
        User user = User.of("test@test.com", "encodedPassword", "nickname", Role.USER);
        String expectedToken = "jwt-token";

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.createToken(any(), any(), any())).thenReturn(expectedToken);

        // 실행
        String token = userCommandService.login(requestDto);

        // 검증
        assertThat(token).isEqualTo(expectedToken);
        verify(jwtUtil).createToken(user.getId(), user.getEmail(), user.getRole());
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    void 로그인_실패_사용자없음() {
        // 준비
        LoginRequestDto requestDto = new LoginRequestDto("test@test.com", "password123!");
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());

        // 실행 & 검증
        assertThrows(IllegalArgumentException.class, () -> {
            userCommandService.login(requestDto);
        });
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void 로그인_실패_비밀번호불일치() {
        // 준비
        LoginRequestDto requestDto = new LoginRequestDto("test@test.com", "password123!");
        User user = User.of("test@test.com", "encodedPassword", "nickname", Role.USER);

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(false);

        // 실행 & 검증
        assertThrows(IllegalArgumentException.class, () -> {
            userCommandService.login(requestDto);
        });
    }

    @Test
    @DisplayName("회원탈퇴 성공")
    void 회원탈퇴_성공() {
        // 준비
        Long userId = 1L;
        User user = User.of("test@test.com", "encodedPassword", "nickname", Role.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // 실행
        userCommandService.deleteUser(userId);

        // 검증
        verify(userRepository).findById(userId);
        // user.delete()가 호출되었는지 확인하는 것은 user 객체의 상태를 확인해야 하므로, 
        // 이 테스트에서는 서비스가 repository를 올바르게 호출하는지만 검증합니다.
    }

    @Test
    @DisplayName("회원탈퇴 실패 - 사용자 없음")
    void 회원탈퇴_실패_사용자없음() {
        // 준비
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // 실행 & 검증
        assertThrows(IllegalArgumentException.class, () -> {
            userCommandService.deleteUser(userId);
        });
    }
}
