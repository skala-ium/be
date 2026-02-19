package com.example.skala_ium.course.dto.response;

import lombok.Builder;

@Builder
public record CourseListResponse(
    Long courseId,
    String courseName,
    String semester,
    String professorName,
    long studentCount
) {
}
