package com.example.skala_ium.submission.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record VerificationResponse(
    Long submissionId,
    List<VerificationResultResponse> results,
    boolean overallMet
) {
}
