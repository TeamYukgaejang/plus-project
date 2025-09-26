package org.example.plusproject.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.common.jwt.JwtUtil;
import org.example.plusproject.domain.user.dto.request.LoginRequestDto;
import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.example.plusproject.domain.user.dto.response.SignUpResponseDto;
import org.example.plusproject.domain.user.service.command.UserCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto responseDto = userCommandService.signUp(requestDto);
        return ApiResponse.created(responseDto, "회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        String token = userCommandService.login(requestDto);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        return ApiResponse.success(null, "로그인 성공");
    }
}
