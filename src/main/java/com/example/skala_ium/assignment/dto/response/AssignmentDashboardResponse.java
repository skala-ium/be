package com.example.skala_ium.assignment.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AssignmentDashboardResponse(
    Long assignmentId,
    String title,
    LocalDateTime deadline,
    long submissionCount,
    long totalStudents,
    long unsubmittedCount
) {
}
