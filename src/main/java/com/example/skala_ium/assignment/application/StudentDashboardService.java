package com.example.skala_ium.assignment.application;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import com.example.skala_ium.assignment.dto.response.StudentAssignmentResponse;
import com.example.skala_ium.assignment.dto.response.dashboard.AssignmentBlock;
import com.example.skala_ium.assignment.dto.response.dashboard.StudentDashboardResponse;
import com.example.skala_ium.assignment.infrastructure.AssignmentRepository;
import com.example.skala_ium.clazz.domain.entity.Clazz;
import com.example.skala_ium.global.auth.security.Authenticatable;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.submission.domain.entity.Submission;
import com.example.skala_ium.submission.infrastructure.SubmissionRepository;
import com.example.skala_ium.user.domain.entity.Student;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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

    public StudentDashboardResponse getStudentDashboard(Authenticatable authenticatable) {
        Student student = requireStudent(authenticatable);
        Clazz clazz = student.getClazz();
        if (clazz == null) {
            throw new CustomException(ErrorType.COURSE_NOT_FOUND);
        }

        List<Assignment> assignments = assignmentRepository.findByClazzId(clazz.getId(), Pageable.unpaged())
            .getContent();

        List<Submission> submissions = submissionRepository.findByStudentId(student.getId());
        Map<UUID, Submission> submissionMap = submissions.stream()
            .collect(Collectors.toMap(s -> s.getAssignment().getId(), s -> s));

        List<AssignmentBlock> unsubmitted = new ArrayList<>();
        List<AssignmentBlock> submitted = new ArrayList<>();

        for (Assignment assignment : assignments) {
            Submission submission = submissionMap.get(assignment.getId());
            AssignmentBlock block = createAssignmentBlock(assignment, submission);

            if (submission == null) {
                unsubmitted.add(block);
            } else {
                submitted.add(block);
            }
        }

        unsubmitted.sort(Comparator.comparing(AssignmentBlock::deadline));
        submitted.sort(Comparator.comparing(AssignmentBlock::submittedAt, Comparator.nullsLast(Comparator.reverseOrder())));

        return StudentDashboardResponse.builder()
            .studentName(student.getName())
            .className(clazz.getClassName())
            .unsubmittedAssignments(unsubmitted)
            .submittedAssignments(submitted)
            .build();
    }

    public List<StudentAssignmentResponse> getMyAssignments(Authenticatable authenticatable, UUID courseId) {
        List<Assignment> assignments = assignmentRepository.findByClazzId(courseId, Pageable.unpaged())
            .getContent();

        return assignments.stream()
            .map(assignment -> {
                Optional<Submission> submission = submissionRepository
                    .findByAssignmentIdAndStudentId(assignment.getId(), authenticatable.getId());
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

    private AssignmentBlock createAssignmentBlock(Assignment assignment, Submission submission) {
        return AssignmentBlock.builder()
            .assignmentId(assignment.getId())
            .title(assignment.getTitle())
            .professorName(assignment.getProfessor() != null ? assignment.getProfessor().getName() : "미지정")
            .deadline(assignment.getDeadline())
            .dDay(calculateDDay(assignment.getDeadline()))
            .status(submission != null ? submission.getStatus().name() : "NOT_SUBMITTED")
            .submittedAt(submission != null ? submission.getSubmittedAt() : null)
            .topic(assignment.getTopic())
            .build();
    }

    private String calculateDDay(LocalDateTime deadline) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), deadline.toLocalDate());
        if (days < 0) return "기한 지남";
        if (days == 0) return "D-Day (마감 임박)";
        return "D-" + days;
    }

    private Student requireStudent(Authenticatable authenticatable) {
        if (authenticatable instanceof Student student) {
            return student;
        }
        throw new CustomException(ErrorType.INVALID_ROLE);
    }
}
