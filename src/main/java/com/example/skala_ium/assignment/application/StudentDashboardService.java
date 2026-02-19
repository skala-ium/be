package com.example.skala_ium.assignment.application;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import com.example.skala_ium.assignment.dto.response.StudentAssignmentResponse;
import com.example.skala_ium.assignment.infrastructure.AssignmentRepository;
import com.example.skala_ium.submission.domain.entity.Submission;
import com.example.skala_ium.submission.infrastructure.SubmissionRepository;
import com.example.skala_ium.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentDashboardService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;

    public List<StudentAssignmentResponse> getMyAssignments(User user, Long courseId) {
        List<Assignment> assignments = assignmentRepository.findByCourseId(courseId, Pageable.unpaged())
            .getContent();

        return assignments.stream()
            .map(assignment -> {
                Optional<Submission> submission = submissionRepository
                    .findByAssignmentIdAndStudentId(assignment.getId(), user.getId());
                boolean submitted = submission.isPresent();
                String status = submitted ? submission.get().getStatus().name() : "NOT_SUBMITTED";

                return StudentAssignmentResponse.builder()
                    .assignmentId(assignment.getId())
                    .title(assignment.getTitle())
                    .deadline(assignment.getDeadline())
                    .submitted(submitted)
                    .status(status)
                    .build();
            })
            .toList();
    }
}
