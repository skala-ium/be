package com.example.skala_ium.submission.infrastructure;

import com.example.skala_ium.submission.domain.entity.Submission;
import com.example.skala_ium.submission.domain.entity.SubmissionStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    long countByAssignmentId(UUID assignmentId);

    Optional<Submission> findByAssignmentIdAndStudentId(UUID assignmentId, UUID studentId);

    List<Submission> findByAssignmentId(UUID assignmentId);

    List<Submission> findByStudentId(UUID studentId);

    boolean existsByAssignmentIdAndStudentId(UUID assignmentId, UUID studentId);

    @Query("SELECT COUNT(s) FROM Submission s WHERE s.assignment.professor.id = :professorId")
    long countByAssignmentProfessorId(@Param("professorId") UUID professorId);

    @Query("SELECT COUNT(s) FROM Submission s WHERE s.assignment.professor.id = :professorId AND s.status = :status")
    long countPendingByAssignmentProfessorId(@Param("professorId") UUID professorId, @Param("status") SubmissionStatus status);

    @Query("SELECT s FROM Submission s JOIN FETCH s.student JOIN FETCH s.assignment WHERE s.assignment.professor.id = :professorId ORDER BY s.submittedAt DESC")
    List<Submission> findRecentByAssignmentProfessorId(@Param("professorId") UUID professorId, Pageable pageable);

    @Query("SELECT s.assignment.id, COUNT(s) FROM Submission s WHERE s.assignment.id IN :assignmentIds GROUP BY s.assignment.id")
    List<Object[]> countGroupByAssignmentIds(@Param("assignmentIds") List<UUID> assignmentIds);
}
