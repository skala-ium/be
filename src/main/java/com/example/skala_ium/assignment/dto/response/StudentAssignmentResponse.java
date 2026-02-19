package com.example.skala_ium.assignment.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record StudentAssignmentResponse(
    Long assignmentId,
    String title,
    LocalDateTime deadline,
    boolean submitted,
    String status
) {
}
