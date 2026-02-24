package com.example.skala_ium.user.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserInfoResponse(
        UUID userId,
        String name,
        String principal, // professor: email, student: slackUserId
        String role
) {
}