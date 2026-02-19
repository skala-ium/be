package com.example.skala_ium.user.api;

import static com.example.skala_ium.global.auth.jwt.entity.TokenType.REFRESH_TOKEN;

import com.example.skala_ium.global.response.ApiResponse;
import com.example.skala_ium.global.response.type.SuccessType;
import com.example.skala_ium.user.application.AuthService;
import com.example.skala_ium.user.dto.request.LoginRequest;
import com.example.skala_ium.user.dto.request.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/users/sign-up")
    public ApiResponse<?> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ApiResponse.success(SuccessType.CREATED);
    }

    @Operation(summary = "로그인")
    @PostMapping("/auth/login")
    public ApiResponse<?> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {
        authService.login(request, response);
        return ApiResponse.success(SuccessType.OK);
    }

    @Operation(summary = "토큰 갱신")
    @PostMapping("/auth/refresh")
    public ApiResponse<?> refresh(
        @CookieValue(name = "refreshToken", required = false) String refreshToken,
        HttpServletResponse response
    ) {
        authService.refresh(refreshToken, response);
        return ApiResponse.success(SuccessType.OK);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/auth/logout")
    public ApiResponse<?> logout(HttpServletResponse response) {
        authService.logout(response);
        return ApiResponse.success(SuccessType.OK);
    }
}
