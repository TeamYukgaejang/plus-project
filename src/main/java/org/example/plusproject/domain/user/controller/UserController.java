package org.example.plusproject.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.user.dto.request.SignUpRequestDto;
import org.example.plusproject.domain.user.entity.User;
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

    private final UserCommandService userCommandService; // Changed field name and type

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signUp(@RequestBody SignUpRequestDto requestDto) {
        User createdUser = userCommandService.signUp(requestDto);
        return ApiResponse.created(createdUser, "회원가입 성공");
    }
}
