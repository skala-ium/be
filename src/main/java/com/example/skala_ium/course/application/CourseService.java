package com.example.skala_ium.course.application;

import com.example.skala_ium.course.domain.entity.Course;
import com.example.skala_ium.course.domain.entity.Enrollment;
import com.example.skala_ium.course.dto.response.CourseDetailResponse;
import com.example.skala_ium.course.dto.response.CourseListResponse;
import com.example.skala_ium.course.dto.response.StudentInfoResponse;
import com.example.skala_ium.course.infrastructure.CourseRepository;
import com.example.skala_ium.course.infrastructure.EnrollmentRepository;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.user.domain.entity.Professor;
import com.example.skala_ium.user.domain.entity.Student;
import com.example.skala_ium.user.domain.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public List<CourseListResponse> getMyCourses(User user) {
        List<Course> courses;

        if (user instanceof Professor) {
            courses = courseRepository.findByProfessorId(user.getId());
        } else if (user instanceof Student) {
            courses = enrollmentRepository.findByStudentId(user.getId()).stream()
                .map(Enrollment::getCourse)
                .toList();
        } else {
            throw new CustomException(ErrorType.INVALID_ROLE);
        }

        return courses.stream()
            .map(course -> CourseListResponse.builder()
                .courseId(course.getId())
                .courseName(course.getCourseName())
                .semester(course.getSemester())
                .professorName(course.getProfessor().getName())
                .studentCount(enrollmentRepository.countByCourseId(course.getId()))
                .build())
            .toList();
    }

    public CourseDetailResponse getCourseDetail(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new CustomException(ErrorType.COURSE_NOT_FOUND));

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        List<StudentInfoResponse> students = enrollments.stream()
            .map(enrollment -> {
                Student student = enrollment.getStudent();
                return StudentInfoResponse.builder()
                    .studentId(student.getId())
                    .name(student.getName())
                    .email(student.getEmail())
                    .major(student.getMajor())
                    .build();
            })
            .toList();

        return CourseDetailResponse.builder()
            .courseId(course.getId())
            .courseName(course.getCourseName())
            .semester(course.getSemester())
            .classGroup(course.getClassGroup())
            .professorName(course.getProfessor().getName())
            .studentCount(students.size())
            .students(students)
            .build();
    }
}
