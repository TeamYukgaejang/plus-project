package org.example.plusproject.domain.user.service.command;

import org.example.plusproject.common.jwt.JwtUtil;
import org.example.plusproject.domain.user.dto.request.LoginRequestDto;
import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.example.plusproject.domain.user.entity.User;
import org.example.plusproject.domain.user.enums.Role;
import org.example.plusproject.domain.user.exception.EmailDuplicatedException;
import org.example.plusproject.domain.user.exception.LoginFailedException;
import org.example.plusproject.domain.user.exception.NicknameDuplicatedException;
import org.example.plusproject.domain.user.exception.UserNotFoundException;
import org.example.plusproject.domain.user.repository.UserRepository;
import org.example.plusproject.domain.user.service.query.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserQueryService userQueryService;

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
        String encodedPassword = "encodedPassword";
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        when(userQueryService.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userQueryService.existsByNickname(requestDto.getNickname())).thenReturn(false);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 실행
        userCommandService.signUp(requestDto);

        // 검증
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getEmail()).isEqualTo(requestDto.getEmail());
        assertThat(capturedUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(capturedUser.getNickname()).isEqualTo(requestDto.getNickname());
        assertThat(capturedUser.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void 회원가입_실패_이메일중복() {
        // 준비
        SignUpRequestDto requestDto = new SignUpRequestDto("test@test.com", "password123!", "nickname");
        assertThrows(EmailDuplicatedException.class, () -> userCommandService.signUp(requestDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void 회원가입_실패_닉네임중복() {
        // 준비
        SignUpRequestDto requestDto = new SignUpRequestDto("test@test.com", "password123!", "nickname");
        when(userQueryService.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userQueryService.existsByNickname(requestDto.getNickname())).thenReturn(true);

        // 실행 & 검증
        assertThrows(NicknameDuplicatedException.class, () -> userCommandService.signUp(requestDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("로그인 성공")
    void 로그인_성공() {
        // 준비
        LoginRequestDto requestDto = new LoginRequestDto("test@test.com", "password123!");
        User user = User.of("test@test.com", "encodedPassword", "nickname", Role.USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        String expectedToken = "jwt-token";

        when(userQueryService.findUserByEmail(requestDto.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole())).thenReturn(expectedToken);

        // 실행
        String token = userCommandService.login(requestDto);

        // 검증
        assertThat(token).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    void 로그인_실패_사용자없음() {
        // 준비
        LoginRequestDto requestDto = new LoginRequestDto("test@test.com", "password123!");
        assertThrows(LoginFailedException.class, () -> userCommandService.login(requestDto));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void 로그인_실패_비밀번호불일치() {
        // 준비
        LoginRequestDto requestDto = new LoginRequestDto("test@test.com", "password123!");
        User user = User.of("test@test.com", "encodedPassword", "nickname", Role.USER);

        when(userQueryService.findUserByEmail(requestDto.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(false);

        // 실행 & 검증
        assertThrows(LoginFailedException.class, () -> userCommandService.login(requestDto));
    }

    @Test
    @DisplayName("회원탈퇴 성공")
    void 회원탈퇴_성공() {
        // 준비
        Long userId = 1L;
        User user = User.of("test@test.com", "encodedPassword", "nickname", Role.USER);
        User spiedUser = spy(user);

        when(userQueryService.findUserById(userId)).thenReturn(spiedUser);

        // 실행
        userCommandService.deleteUser(userId);

        // 검증
        verify(userQueryService).findUserById(userId);
        verify(spiedUser).delete();
    }

    @Test
    @DisplayName("회원탈퇴 실패 - 사용자 없음")
    void 회원탈퇴_실패_사용자없음() {
        // 준비
        Long userId = 1L;
        when(userQueryService.findUserById(userId)).thenThrow(new UserNotFoundException());

        // 실행 & 검증
        assertThrows(UserNotFoundException.class, () -> userCommandService.deleteUser(userId));
    }
}
