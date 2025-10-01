package org.example.plusproject.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.like.service.command.LikeCommandServiceImpl;
import org.example.plusproject.domain.like.service.query.LikeQueryServiceImpl;
import org.example.plusproject.domain.user.dto.security.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeCommandServiceImpl likeCommandService;

    @PostMapping("/reviews/{reviewId}/likes")
    public ResponseEntity<ApiResponse<Object>> toggleLike(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long reviewId
    ) {
        return ResponseEntity
                .status(200)
                .body(likeCommandService.toggleLike(authUser, reviewId));
    }
}
