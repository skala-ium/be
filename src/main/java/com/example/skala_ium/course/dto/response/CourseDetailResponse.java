package com.example.skala_ium.course.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record CourseDetailResponse(
    Long courseId,
    String courseName,
    String semester,
    String classGroup,
    String professorName,
    long studentCount,
    List<StudentInfoResponse> students
) {
}
