package com.example.skala_ium.user.infrastructure;

import com.example.skala_ium.user.domain.entity.Professor;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {

    Optional<Professor> findByEmail(String email);

    boolean existsByEmail(String email);
}
