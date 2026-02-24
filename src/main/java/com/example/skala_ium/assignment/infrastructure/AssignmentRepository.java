package com.example.skala_ium.assignment.infrastructure;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    @EntityGraph(attributePaths = {"classes", "professor"})
    Page<Assignment> findByClazzId(UUID classId, Pageable pageable);

    @EntityGraph(attributePaths = {"requirements", "classes", "professor"})
    Optional<Assignment> findWithDetailsById(UUID id);
}
