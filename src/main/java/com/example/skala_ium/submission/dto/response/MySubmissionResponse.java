package com.example.skala_ium.submission.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MySubmissionResponse(
    Long submissionId,
    String assignmentTitle,
    LocalDateTime submittedAt,
    String status
) {
}
