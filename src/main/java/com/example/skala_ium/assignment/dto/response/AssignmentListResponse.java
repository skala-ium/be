package com.example.skala_ium.assignment.dto.response;

import com.example.skala_ium.class_.domain.ClassGroup;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AssignmentListResponse(
    Long assignmentId,
    String title,
    ClassGroup classGroup,
    LocalDateTime deadline,
    double submissionRate,
    long submissionCount,
    long totalStudents,
    String professorName,
    String description,
    String topic
) {
}
