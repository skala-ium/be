package com.example.skala_ium.assignment.dto.response;

import com.example.skala_ium.clazz.domain.ClassGroup;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record AssignmentListResponse(
    UUID assignmentId,
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
