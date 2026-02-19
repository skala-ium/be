package com.example.skala_ium.assignment.infrastructure;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @EntityGraph(attributePaths = {"course", "course.professor"})
    Page<Assignment> findByCourseId(Long courseId, Pageable pageable);

    @EntityGraph(attributePaths = {"requirements", "course", "course.professor"})
    Optional<Assignment> findWithDetailsById(Long id);
}
