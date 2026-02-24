package com.example.skala_ium.user.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StudentInfoResponse(
        UUID studentId,
        String name,
        String slackUserId,
        String major
) {
}