package com.example.skala_ium.class_.dto.response;

import com.example.skala_ium.class_.domain.ClassGroup;
import java.util.List;
import lombok.Builder;

@Builder
public record CourseDetailResponse(
    Long courseId,
    String courseName,
    Integer generation,
    ClassGroup classGroup,
    long studentCount,
    List<StudentInfoResponse> students
) {
}
