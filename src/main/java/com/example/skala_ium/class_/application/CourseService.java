package com.example.skala_ium.class_.application;

import com.example.skala_ium.class_.domain.entity.Course;
import com.example.skala_ium.class_.dto.response.CourseDetailResponse;
import com.example.skala_ium.class_.dto.response.CourseListResponse;
import com.example.skala_ium.class_.dto.response.StudentInfoResponse;
import com.example.skala_ium.class_.infrastructure.CourseRepository;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.user.domain.entity.Student;
import com.example.skala_ium.user.infrastructure.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public List<CourseListResponse> getAllCourses() {
        return courseRepository.findAll().stream()
            .map(course -> CourseListResponse.builder()
                .courseId(course.getId())
                .courseName(course.getClassName())
                .generation(course.getGeneration())
                .studentCount(studentRepository.countByCourseId(course.getId()))
                .build())
            .toList();
    }

    public CourseDetailResponse getCourseDetail(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new CustomException(ErrorType.COURSE_NOT_FOUND));

        List<Student> students = studentRepository.findByCourseId(courseId);
        List<StudentInfoResponse> studentInfos = students.stream()
            .map(student -> StudentInfoResponse.builder()
                .studentId(student.getId())
                .name(student.getName())
                .slackUserId(student.getSlackUserId())
                .major(student.getMajor())
                .build())
            .toList();

        return CourseDetailResponse.builder()
            .courseId(course.getId())
            .courseName(course.getClassName())
            .generation(course.getGeneration())
            .classGroup(course.getClassGroup())
            .studentCount(studentInfos.size())
            .students(studentInfos)
            .build();
    }
}