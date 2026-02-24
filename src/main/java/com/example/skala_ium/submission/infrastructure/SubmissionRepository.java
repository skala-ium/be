package com.example.skala_ium.submission.infrastructure;

import com.example.skala_ium.submission.domain.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    long countByAssignmentId(UUID assignmentId);

    Optional<Submission> findByAssignmentIdAndStudentId(UUID assignmentId, UUID studentId);

    List<Submission> findByAssignmentId(UUID assignmentId);

    List<Submission> findByStudentId(UUID studentId);

    boolean existsByAssignmentIdAndStudentId(UUID assignmentId, UUID studentId);
}
