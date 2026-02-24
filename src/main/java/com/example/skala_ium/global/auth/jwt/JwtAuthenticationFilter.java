package com.example.skala_ium.global.auth.jwt;

import static com.example.skala_ium.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;

import com.example.skala_ium.global.auth.jwt.component.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> ALLOWED_PATHS = List.of(
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/api/v1/auth/**",
        "/api/v1/users/sign-up"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return ALLOWED_PATHS.stream()
            .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = jwtTokenProvider.extractTokenFromCookies(request, ACCESS_TOKEN);

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                log.warn("JWT 인증 처리 중 예외 발생, 비인증 상태로 진행: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }


}