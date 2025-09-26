package org.example.plusproject.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.plusproject.common.security.JwtAuthenticationToken;
import org.example.plusproject.domain.user.dto.security.AuthUser;
import org.example.plusproject.domain.user.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getJwtFromHeader(request);

        if (tokenValue != null) {
            if (jwtUtil.validateToken(tokenValue)) {
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                setAuthentication(info);
            } else {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(Claims claims) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Long userId = Long.valueOf(claims.getSubject());
        String email = claims.get("email", String.class);
        Role role = Role.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY, String.class));
        AuthUser authUser = new AuthUser(userId, email, role);
        Authentication authentication = new JwtAuthenticationToken(authUser);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 에러 응답 전송
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        java.util.Map<String, Object> errorResponse = new java.util.HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
