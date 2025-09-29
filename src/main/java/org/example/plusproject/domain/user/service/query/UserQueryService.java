package org.example.plusproject.domain.user.service.query;

import org.example.plusproject.domain.user.entity.User;

public interface UserQueryService {
    User findUserById(Long userId);
    User findUserByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
