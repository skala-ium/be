package com.example.skala_ium.submission.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SubmissionStatusResponse(
    Long studentId,
    String studentName,
    boolean submitted,
    LocalDateTime submittedAt
) {
}
