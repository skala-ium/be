package com.example.skala_ium.class_.infrastructure;

import com.example.skala_ium.class_.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
