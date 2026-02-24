package com.example.skala_ium.user.infrastructure;

import com.example.skala_ium.user.domain.entity.Student;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findBySlackUserId(String slackUserId);

    boolean existsBySlackUserId(String slackUserId);

    List<Student> findByClazzId(UUID classId);

    long countByClazzId(UUID classId);
}