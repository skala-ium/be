package com.example.skala_ium.assignment.infrastructure;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    @EntityGraph(attributePaths = {"clazz", "professor"})
    Page<Assignment> findByClazzId(UUID classId, Pageable pageable);

    @EntityGraph(attributePaths = {"clazz", "professor"})
    Page<Assignment> findByClazzIdAndProfessorId(UUID classId, UUID professorId, Pageable pageable);

    @EntityGraph(attributePaths = {"requirements", "clazz", "professor"})
    Optional<Assignment> findByIdAndProfessorId(UUID id, UUID professorId);

    long countByProfessorId(UUID professorId);

    @Query("SELECT DISTINCT a.clazz.id FROM Assignment a WHERE a.professor.id = :professorId")
    List<UUID> findDistinctClazzIdByProfessorId(@Param("professorId") UUID professorId);

    long countByClazzIdAndProfessorId(UUID classId, UUID professorId);

    @EntityGraph(attributePaths = {"clazz", "professor"})
    @Query("SELECT a FROM Assignment a WHERE a.professor.id = :professorId ORDER BY a.deadline DESC")
    List<Assignment> findTop5ByProfessorId(@Param("professorId") UUID professorId, Pageable pageable);
}
