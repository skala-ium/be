package com.example.skala_ium.submission.application;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import com.example.skala_ium.assignment.infrastructure.AssignmentRepository;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.submission.domain.entity.Submission;
import com.example.skala_ium.submission.domain.entity.SubmissionStatus;
import com.example.skala_ium.submission.dto.request.CreateSubmissionRequest;
import com.example.skala_ium.submission.dto.response.MySubmissionResponse;
import com.example.skala_ium.submission.dto.response.SubmissionResponse;
import com.example.skala_ium.submission.dto.response.SubmissionStatusResponse;
import com.example.skala_ium.submission.infrastructure.SubmissionRepository;
import com.example.skala_ium.user.domain.entity.Student;
import com.example.skala_ium.user.infrastructure.StudentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void submitAssignment(UUID assignmentId, Student student, CreateSubmissionRequest request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new CustomException(ErrorType.ASSIGNMENT_NOT_FOUND));

        if (LocalDateTime.now().isAfter(assignment.getDeadline())) {
            throw new CustomException(ErrorType.DEADLINE_PASSED);
        }

        if (submissionRepository.existsByAssignmentIdAndStudentId(assignmentId, student.getId())) {
            throw new CustomException(ErrorType.ALREADY_SUBMITTED);
        }

        Submission submission = Submission.builder()
            .assignment(assignment)
            .student(student)
            .contentText(request.contentText())
            .fileUrl(request.fileUrl())
            .fileName(request.fileName())
            .submittedAt(LocalDateTime.now())
            .status(SubmissionStatus.SUBMITTED)
            .build();

        submissionRepository.save(submission);
    }

    public List<SubmissionResponse> getSubmissionsByAssignment(UUID assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId).stream()
            .map(submission -> SubmissionResponse.builder()
                .submissionId(submission.getId())
                .studentName(submission.getStudent().getName())
                .contentText(submission.getContentText())
                .fileUrl(submission.getFileUrl())
                .submittedAt(submission.getSubmittedAt())
                .status(submission.getStatus().name())
                .build())
            .toList();
    }

    public List<SubmissionStatusResponse> getSubmissionStatus(UUID assignmentId, UUID courseId) {
        List<Student> students = studentRepository.findByClazzId(courseId);

        return students.stream()
            .map(student -> {
                Optional<Submission> submission = submissionRepository
                    .findByAssignmentIdAndStudentId(assignmentId, student.getId());

                return SubmissionStatusResponse.builder()
                    .studentId(student.getId())
                    .studentName(student.getName())
                    .submitted(submission.isPresent())
                    .submittedAt(submission.map(Submission::getSubmittedAt).orElse(null))
                    .build();
            })
            .toList();
    }

    public List<MySubmissionResponse> getMySubmissions(Student student) {
        return submissionRepository.findByStudentId(student.getId()).stream()
            .map(submission -> MySubmissionResponse.builder()
                .submissionId(submission.getId())
                .assignmentTitle(submission.getAssignment().getTitle())
                .submittedAt(submission.getSubmittedAt())
                .status(submission.getStatus().name())
                .build())
            .toList();
    }

    public SubmissionResponse getMySubmissionForAssignment(UUID assignmentId, Student student) {
        Submission submission = submissionRepository
            .findByAssignmentIdAndStudentId(assignmentId, student.getId())
            .orElseThrow(() -> new CustomException(ErrorType.SUBMISSION_NOT_FOUND));

        return SubmissionResponse.builder()
            .submissionId(submission.getId())
            .studentName(submission.getStudent().getName())
            .contentText(submission.getContentText())
            .fileUrl(submission.getFileUrl())
            .submittedAt(submission.getSubmittedAt())
            .status(submission.getStatus().name())
            .build();
    }
}