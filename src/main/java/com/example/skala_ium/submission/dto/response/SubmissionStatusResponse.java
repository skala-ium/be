package com.example.skala_ium.submission.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SubmissionStatusResponse(
    UUID studentId,
    String studentName,
    boolean submitted,
    LocalDateTime submittedAt
) {
}
