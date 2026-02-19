package com.example.skala_ium.course.dto.response;

import lombok.Builder;

@Builder
public record StudentInfoResponse(
    Long studentId,
    String name,
    String email,
    String major
) {
}
