package com.example.skala_ium.assignment.dto.response.dashboard;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record AssignmentBlock(
    UUID assignmentId,
    String title,
    String professorName,
    LocalDateTime deadline,
    String dDay,
    String status,
    LocalDateTime submittedAt,
    String topic
) {
}
