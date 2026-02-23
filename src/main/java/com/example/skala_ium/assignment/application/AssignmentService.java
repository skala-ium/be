package com.example.skala_ium.assignment.application;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import com.example.skala_ium.assignment.dto.response.AssignmentDashboardResponse;
import com.example.skala_ium.assignment.dto.response.AssignmentDetailResponse;
import com.example.skala_ium.assignment.dto.response.AssignmentListResponse;
import com.example.skala_ium.assignment.dto.response.RequirementResponse;
import java.util.List;
import com.example.skala_ium.assignment.infrastructure.AssignmentRepository;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.submission.infrastructure.SubmissionRepository;
import com.example.skala_ium.user.infrastructure.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public Page<AssignmentListResponse> getAssignments(Long classId, Pageable pageable) {
        long totalStudents = studentRepository.countByCourseId(classId);

        return assignmentRepository.findByCourseId(classId, pageable)
            .map(assignment -> {
                long submissionCount = submissionRepository.countByAssignmentId(assignment.getId());
                double submissionRate = totalStudents > 0
                    ? (double) submissionCount / totalStudents * 100 : 0;
                return AssignmentListResponse.builder()
                    .assignmentId(assignment.getId())
                    .title(assignment.getTitle())
                    .classGroup(assignment.getCourse().getClassGroup())
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

    public List<AssignmentDashboardResponse> getAssignmentDashboard(Long courseId) {
        long totalStudents = studentRepository.countByCourseId(courseId);
        List<Assignment> assignments = assignmentRepository.findByCourseId(courseId,
            org.springframework.data.domain.Pageable.unpaged()).getContent();

        return assignments.stream()
            .map(assignment -> {
                long submissionCount = submissionRepository.countByAssignmentId(assignment.getId());
                return AssignmentDashboardResponse.builder()
                    .assignmentId(assignment.getId())
                    .title(assignment.getTitle())
                    .deadline(assignment.getDeadline())
                    .submissionCount(submissionCount)
                    .totalStudents(totalStudents)
                    .unsubmittedCount(totalStudents - submissionCount)
                    .build();
            })
            .toList();
    }

    public AssignmentDetailResponse getAssignmentDetail(Long assignmentId) {
        Assignment assignment = assignmentRepository.findWithDetailsById(assignmentId)
            .orElseThrow(() -> new CustomException(ErrorType.ASSIGNMENT_NOT_FOUND));

        return AssignmentDetailResponse.builder()
            .assignmentId(assignment.getId())
            .courseName(assignment.getCourse().getClassName())
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