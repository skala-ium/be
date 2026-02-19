package com.example.skala_ium.submission.dto.response;

import lombok.Builder;

@Builder
public record VerificationResultResponse(
    Long requirementId,
    String content,
    boolean isMet,
    String feedback
) {
}
