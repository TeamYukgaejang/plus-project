package org.example.plusproject.common.config;

import lombok.RequiredArgsConstructor;
import org.example.plusproject.common.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // 세션 관리 설정: STATELESS로 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 요청 권한 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/api/auth/signup", "/api/auth/login").permitAll() // /api/auth/signup, /api/auth/login 요청은 모두 접근 허용
                        .requestMatchers("/api/v1/products/**").permitAll() // 상품 조회 API는 인증 불필요
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // /api/v1/admin/ 으로 시작하는 요청은 ADMIN 권한 필요
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 처리
        );

        // JWT 방식에서는 필요 없는 formLogin, httpBasic 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 필터 순서 설정: 우리가 만든 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}