package com.example.skala_ium.clazz.application;


import com.example.skala_ium.clazz.domain.entity.Clazz;
import com.example.skala_ium.clazz.dto.response.ClassDetailResponse;
import com.example.skala_ium.clazz.dto.response.ClassListResponse;
import com.example.skala_ium.clazz.infrastructure.ClassRepository;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.user.domain.entity.Student;
import com.example.skala_ium.user.dto.response.StudentInfoResponse;
import com.example.skala_ium.user.infrastructure.StudentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassService {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;

    public List<ClassListResponse> getAllClasses() {
        return classRepository.findAll().stream()
            .map(clazz -> ClassListResponse.builder()
                .classId(clazz.getId())
                .className(clazz.getClassName())
                .generation(clazz.getGeneration())
                .studentCount(studentRepository.countByClazzId(clazz.getId()))
                .build())
            .toList();
    }

    public ClassDetailResponse getClassDetail(UUID classId) {
        Clazz clazz = classRepository.findById(classId)
            .orElseThrow(() -> new CustomException(ErrorType.COURSE_NOT_FOUND));

        List<Student> students = studentRepository.findByClazzId(classId);
        List<StudentInfoResponse> studentInfos = students.stream()
            .map(student -> StudentInfoResponse.builder()
                .studentId(student.getId())
                .name(student.getName())
                .slackUserId(student.getSlackUserId())
                .major(student.getMajor())
                .build())
            .toList();

        return ClassDetailResponse.builder()
            .courseId(clazz.getId())
            .courseName(clazz.getClassName())
            .generation(clazz.getGeneration())
            .classGroup(clazz.getClassGroup())
            .studentCount(studentInfos.size())
            .students(studentInfos)
            .build();
    }
}