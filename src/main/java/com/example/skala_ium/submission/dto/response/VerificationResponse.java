package com.example.skala_ium.submission.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record VerificationResponse(
    UUID submissionId,
    List<VerificationResultResponse> results,
    boolean overallMet
) {
}
