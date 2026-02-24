package com.example.skala_ium.assignment.dto.response.dashboard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DashboardResponse(
    Summary summary,
    List<RecentSubmission> recentSubmissions,
    List<AssignmentSubmissionRate> assignmentSubmissionRates
) {

    @Builder
    public record Summary(
        long totalAssignments,
        long pendingReviewCount,
        long overallSubmissionRate
    ) {
    }

    @Builder
    public record RecentSubmission(
        String studentName,
        String assignmentTitle,
        LocalDateTime submittedAt,
        String fileName,
        String fileUrl
    ) {
    }

    @Builder
    public record AssignmentSubmissionRate(
        UUID assignmentId,
        String assignmentName,
        long submissionRate
    ) {
    }
}
