package com.example.skala_ium.assignment.application;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import com.example.skala_ium.assignment.dto.response.AssignmentDetailResponse;
import com.example.skala_ium.assignment.dto.response.AssignmentListResponse;
import com.example.skala_ium.assignment.dto.response.RequirementResponse;
import com.example.skala_ium.assignment.dto.response.dashboard.DashboardResponse;
import com.example.skala_ium.assignment.dto.response.dashboard.DashboardResponse.Summary;
import com.example.skala_ium.assignment.infrastructure.AssignmentRepository;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.submission.domain.entity.SubmissionStatus;
import com.example.skala_ium.submission.infrastructure.SubmissionRepository;
import com.example.skala_ium.user.infrastructure.StudentRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;

    public Page<AssignmentListResponse> getAssignments(UUID classId, UUID professorId, Pageable pageable) {
        long totalStudents = studentRepository.countByClazzId(classId);

        return assignmentRepository.findByClazzIdAndProfessorId(classId, professorId, pageable)
            .map(assignment -> {
                long submissionCount = submissionRepository.countByAssignmentId(assignment.getId());
                double submissionRate = totalStudents > 0
                    ? (double) submissionCount / totalStudents * 100 : 0;
                return AssignmentListResponse.builder()
                    .assignmentId(assignment.getId())
                    .title(assignment.getTitle())
                    .classGroup(assignment.getClazz().getClassGroup())
                    .deadline(assignment.getDeadline())
                    .submissionRate(submissionRate)
                    .submissionCount(submissionCount)
                    .totalStudents(totalStudents)
                    .professorName(assignment.getProfessor().getName())
                    .description(assignment.getContent())
                    .topic(assignment.getTopic())
                    .build();
            });
    }

    public DashboardResponse getDashboard(UUID professorId) {
        long totalAssignments = assignmentRepository.countByProfessorId(professorId);
        long pendingReviewCount = submissionRepository.countPendingByAssignmentProfessorId(
            professorId, SubmissionStatus.SUBMITTED);
        long totalSubmissions = submissionRepository.countByAssignmentProfessorId(professorId);

        List<UUID> classIds = assignmentRepository.findDistinctClazzIdByProfessorId(professorId);
        long totalPossible = 0;
        for (UUID classId : classIds) {
            long assignmentsInClass = assignmentRepository.countByClazzIdAndProfessorId(classId, professorId);
            long studentsInClass = studentRepository.countByClazzId(classId);
            totalPossible += assignmentsInClass * studentsInClass;
        }
        long overallRate = Math.round(totalPossible > 0 ? (double) totalSubmissions / totalPossible * 100.0 : 0.0);

        Summary summary = Summary.builder()
            .totalAssignments(totalAssignments)
            .pendingReviewCount(pendingReviewCount)
            .overallSubmissionRate(overallRate)
            .build();

        List<DashboardResponse.RecentSubmission> recentSubmissions =
            submissionRepository.findRecentByAssignmentProfessorId(professorId, PageRequest.of(0, 10))
                .stream()
                .map(s -> DashboardResponse.RecentSubmission.builder()
                    .studentName(s.getStudent().getName())
                    .assignmentTitle(s.getAssignment().getTitle())
                    .submittedAt(s.getSubmittedAt())
                    .fileName(s.getFileName())
                    .fileUrl(s.getFileUrl())
                    .build())
                .toList();

        List<Assignment> top5 = assignmentRepository.findTop5ByProfessorId(professorId, PageRequest.of(0, 5));

        List<DashboardResponse.AssignmentSubmissionRate> rates;
        if (top5.isEmpty()) {
            rates = List.of();
        } else {
            List<UUID> assignmentIds = top5.stream().map(Assignment::getId).toList();

            Map<UUID, Long> countMap = submissionRepository
                .countGroupByAssignmentIds(assignmentIds)
                .stream()
                .collect(Collectors.toMap(row -> (UUID) row[0], row -> (Long) row[1]));

            Map<UUID, Long> studentCountByClass = top5.stream()
                .map(a -> a.getClazz().getId())
                .distinct()
                .collect(Collectors.toMap(
                    classId -> classId,
                    studentRepository::countByClazzId
                ));

            rates = top5.stream()
                .map(a -> {
                    long count = countMap.getOrDefault(a.getId(), 0L);
                    long students = studentCountByClass.getOrDefault(a.getClazz().getId(), 0L);
                    long rate = Math.round(students > 0 ? (double) count / students * 100.0 : 0.0);
                    return DashboardResponse.AssignmentSubmissionRate.builder()
                        .assignmentId(a.getId())
                        .assignmentName(a.getTitle())
                        .submissionRate(rate)
                        .build();
                })
                .toList();
        }

        return DashboardResponse.builder()
            .summary(summary)
            .recentSubmissions(recentSubmissions)
            .assignmentSubmissionRates(rates)
            .build();
    }

    public AssignmentDetailResponse getAssignmentDetail(UUID assignmentId, UUID professorId) {
        Assignment assignment = assignmentRepository.findByIdAndProfessorId(assignmentId, professorId)
            .orElseThrow(() -> new CustomException(ErrorType.ASSIGNMENT_NOT_FOUND));

        return AssignmentDetailResponse.builder()
            .assignmentId(assignment.getId())
            .courseName(assignment.getClazz().getClassName())
            .title(assignment.getTitle())
            .description(assignment.getContent())
            .topic(assignment.getTopic())
            .professorName(assignment.getProfessor().getName())
            .deadline(assignment.getDeadline())
            .requirements(assignment.getRequirements().stream()
                .map(req -> new RequirementResponse(req.getId(), req.getContent()))
                .toList())
            .slackLink(assignment.getSlackPostTs())
            .build();
    }
}
