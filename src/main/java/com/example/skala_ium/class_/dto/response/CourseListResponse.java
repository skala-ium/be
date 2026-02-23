package com.example.skala_ium.class_.dto.response;

import lombok.Builder;

@Builder
public record CourseListResponse(
    Long courseId,
    String courseName,
    Integer generation,
    long studentCount
) {
}
