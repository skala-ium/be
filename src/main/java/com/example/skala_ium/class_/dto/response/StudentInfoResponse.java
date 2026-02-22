package com.example.skala_ium.class_.dto.response;

import lombok.Builder;

@Builder
public record StudentInfoResponse(
    Long studentId,
    String name,
    String slackUserId,
    String major
) {
}