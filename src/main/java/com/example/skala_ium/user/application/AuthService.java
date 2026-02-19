package com.example.skala_ium.user.application;

import static com.example.skala_ium.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.example.skala_ium.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.example.skala_ium.global.auth.jwt.component.CookieProvider;
import com.example.skala_ium.global.auth.jwt.component.JwtTokenProvider;
import com.example.skala_ium.global.auth.jwt.entity.JwtToken;
import com.example.skala_ium.global.auth.security.Role;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.user.domain.entity.Professor;
import com.example.skala_ium.user.domain.entity.Student;
import com.example.skala_ium.user.domain.entity.User;
import com.example.skala_ium.user.dto.request.LoginRequest;
import com.example.skala_ium.user.dto.request.SignUpRequest;
import com.example.skala_ium.user.infrastructure.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;

    public void signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorType.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        String roleValue = Role.USER.getRole();

        User user;
        if ("PROFESSOR".equalsIgnoreCase(request.role())) {
            user = Professor.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .role(roleValue)
                .department(request.department())
                .build();
        } else if ("STUDENT".equalsIgnoreCase(request.role())) {
            user = Student.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .role(roleValue)
                .major(request.major())
                .build();
        } else {
            throw new CustomException(ErrorType.INVALID_ROLE);
        }

        userRepository.save(user);
    }

    public void login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new CustomException(ErrorType.FAIL_AUTHENTICATION));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorType.FAIL_AUTHENTICATION);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getEmail(), null,
            new com.example.skala_ium.global.auth.security.CustomerDetails(user).getAuthorities()
        );

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        response.addCookie(cookieProvider.createTokenCookie(ACCESS_TOKEN, jwtToken.getAccessToken()));
        response.addCookie(cookieProvider.createTokenCookie(REFRESH_TOKEN, jwtToken.getRefreshToken()));
    }

    public void refresh(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorType.EXPIRED_REFRESH_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthenticationFromRefreshToken(refreshToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        response.addCookie(cookieProvider.createTokenCookie(ACCESS_TOKEN, jwtToken.getAccessToken()));
        response.addCookie(cookieProvider.createTokenCookie(REFRESH_TOKEN, jwtToken.getRefreshToken()));
    }

    public void logout(HttpServletResponse response) {
        response.addCookie(cookieProvider.createExpireCookie(ACCESS_TOKEN));
        response.addCookie(cookieProvider.createExpireCookie(REFRESH_TOKEN));
    }
}
