package org.example.plusproject.domain.like.service.command;


import org.example.plusproject.common.dto.response.ApiResponse;
import org.example.plusproject.domain.user.dto.security.AuthUser;

public interface LikeCommandService {
    ApiResponse<Object> toggleLike(AuthUser authUser, Long reviewId);
}
