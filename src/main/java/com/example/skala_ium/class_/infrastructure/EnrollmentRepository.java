package com.example.skala_ium.class_.infrastructure;

import com.example.skala_ium.class_.domain.entity.Enrollment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    long countByCourseId(Long courseId);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByStudentId(Long studentId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
