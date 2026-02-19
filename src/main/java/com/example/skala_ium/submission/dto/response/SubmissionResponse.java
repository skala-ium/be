package com.example.skala_ium.submission.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SubmissionResponse(
    Long submissionId,
    String studentName,
    String contentText,
    String fileUrl,
    LocalDateTime submittedAt,
    String status
) {
}
