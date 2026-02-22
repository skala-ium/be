package com.example.skala_ium.user.infrastructure;

import com.example.skala_ium.user.domain.entity.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findBySlackUserId(String slackUserId);

    boolean existsBySlackUserId(String slackUserId);

    List<Student> findByCourseId(Long courseId);

    long countByCourseId(Long courseId);
}