package com.example.skala_ium.user.application;

import static com.example.skala_ium.global.auth.jwt.entity.TokenType.ACCESS_TOKEN;
import static com.example.skala_ium.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.example.skala_ium.clazz.domain.entity.Clazz;
import com.example.skala_ium.clazz.infrastructure.ClassRepository;
import com.example.skala_ium.global.auth.jwt.component.CookieProvider;
import com.example.skala_ium.global.auth.jwt.component.JwtTokenProvider;
import com.example.skala_ium.global.auth.jwt.entity.JwtToken;
import com.example.skala_ium.global.auth.security.Authenticatable;
import com.example.skala_ium.global.auth.security.CustomerDetails;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.user.domain.entity.Professor;
import com.example.skala_ium.user.domain.entity.Student;
import com.example.skala_ium.user.dto.request.LoginRequest;
import com.example.skala_ium.user.dto.request.SignUpRequest;
import com.example.skala_ium.user.infrastructure.ProfessorRepository;
import com.example.skala_ium.user.infrastructure.StudentRepository;
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

    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;

    public void signUp(SignUpRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());

        if ("PROFESSOR".equalsIgnoreCase(request.role())) {
            if (request.email() == null || request.email().isBlank()) {
                throw new CustomException(ErrorType.BAD_REQUEST);
            }
            if (professorRepository.existsByEmail(request.email())) {
                throw new CustomException(ErrorType.DUPLICATE_EMAIL);
            }
            Professor professor = Professor.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .build();
            professorRepository.save(professor);

        } else if ("STUDENT".equalsIgnoreCase(request.role())) {
            if (request.slackUserId() == null || request.slackUserId().isBlank()) {
                throw new CustomException(ErrorType.BAD_REQUEST);
            }
            if (studentRepository.existsBySlackUserId(request.slackUserId())) {
                throw new CustomException(ErrorType.DUPLICATE_EMAIL);
            }
            Clazz clazz = null;
            if (request.classId() != null) {
                clazz = classRepository.findById(request.classId())
                    .orElseThrow(() -> new CustomException(ErrorType.COURSE_NOT_FOUND));
            }
            Student student = Student.builder()
                .name(request.name())
                .slackUserId(request.slackUserId())
                .password(encodedPassword)
                .major(request.major())
                .clazz(clazz)
                .build();
            studentRepository.save(student);

        } else {
            throw new CustomException(ErrorType.INVALID_ROLE);
        }
    }

    public void login(LoginRequest request, HttpServletResponse response) {
        Authenticatable authenticatable;

        if ("PROFESSOR".equalsIgnoreCase(request.role())) {
            authenticatable = professorRepository.findByEmail(request.identifier())
                .orElseThrow(() -> new CustomException(ErrorType.FAIL_AUTHENTICATION));
        } else if ("STUDENT".equalsIgnoreCase(request.role())) {
            authenticatable = studentRepository.findBySlackUserId(request.identifier())
                .orElseThrow(() -> new CustomException(ErrorType.FAIL_AUTHENTICATION));
        } else {
            throw new CustomException(ErrorType.INVALID_ROLE);
        }

        if (!passwordEncoder.matches(request.password(), authenticatable.getPassword())) {
            throw new CustomException(ErrorType.FAIL_AUTHENTICATION);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            authenticatable.getPrincipal(), null,
            new CustomerDetails(authenticatable).getAuthorities()
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