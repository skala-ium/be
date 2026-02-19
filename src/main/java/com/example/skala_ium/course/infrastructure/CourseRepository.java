package com.example.skala_ium.course.infrastructure;

import com.example.skala_ium.course.domain.entity.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByProfessorId(Long professorId);
}
