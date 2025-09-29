package org.example.plusproject.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.jwt.JwtUtil;
import org.example.plusproject.domain.user.dto.request.LoginRequestDto;
import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.example.plusproject.domain.user.dto.response.SignUpResponseDto;
import org.example.plusproject.domain.user.dto.security.AuthUser;
import org.example.plusproject.domain.user.exception.UserSuccessCode;
import org.example.plusproject.domain.user.service.command.UserCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        ApiResponse<SignUpResponseDto> response = userCommandService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        String token = userCommandService.login(requestDto);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        ApiResponse<Void> apiResponse = ApiResponse.of(UserSuccessCode.REQUEST_SUCCESS, null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal AuthUser authUser) {
        userCommandService.deleteUser(authUser.getUserId());
        return ResponseEntity.ok(ApiResponse.of(UserSuccessCode.REQUEST_SUCCESS, null));
    }
}
