package com.example.skala_ium.assignment.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record StudentAssignmentResponse(
        UUID assignmentId,
        String title,
        LocalDateTime deadline,
        boolean submitted,
        String status
) {
}
