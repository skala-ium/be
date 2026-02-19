package com.example.skala_ium.assignment.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record AssignmentDetailResponse(
    Long assignmentId,
    String courseName,
    String title,
    String description,
    String week,
    String topic,
    String professorName,
    LocalDateTime deadline,
    List<RequirementResponse> requirements,
    String slackLink
) {
}
