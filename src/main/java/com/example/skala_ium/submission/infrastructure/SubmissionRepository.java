package com.example.skala_ium.submission.infrastructure;

import com.example.skala_ium.submission.domain.entity.Submission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    long countByAssignmentId(Long assignmentId);

    Optional<Submission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    List<Submission> findByAssignmentId(Long assignmentId);

    List<Submission> findByStudentId(Long studentId);

    boolean existsByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
}
