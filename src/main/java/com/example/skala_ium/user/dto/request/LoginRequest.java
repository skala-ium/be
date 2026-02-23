package com.example.skala_ium.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    // Professor: email, Student: slackUserId
    @NotBlank(message = "식별자(이메일 또는 Slack User ID)는 필수입니다.")
    String identifier,

    @NotBlank(message = "비밀번호는 필수입니다.")
    String password,

    @NotBlank(message = "역할은 필수입니다.")
    String role
) {
}
