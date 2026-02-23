package com.example.skala_ium.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
    @NotBlank(message = "이름은 필수입니다.")
    String name,

    @NotBlank(message = "비밀번호는 필수입니다.")
    String password,

    @NotBlank(message = "역할은 필수입니다.")
    String role,

    // Professor 전용 필드
    String email,

    // Student 전용 필드
    String slackUserId,
    String major
) {
}