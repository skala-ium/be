package com.example.skala_ium.assignment.dto.response.dashboard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record StudentDashboardResponse(
    String studentName,
    String className,
    List<AssignmentBlock> unsubmittedAssignments,
    List<AssignmentBlock> submittedAssignments
) {
}
