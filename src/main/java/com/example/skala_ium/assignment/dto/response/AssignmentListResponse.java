package com.example.skala_ium.assignment.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AssignmentListResponse(
    Long assignmentId,
    String title,
    String classGroup,
    LocalDateTime deadline,
    double submissionRate,
    long submissionCount,
    long totalStudents,
    String professorName,
    String description,
    String week,
    String topic
) {
}
