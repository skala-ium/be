package com.example.skala_ium.clazz.dto.response;

import com.example.skala_ium.clazz.domain.ClassGroup;
import com.example.skala_ium.user.dto.response.StudentInfoResponse;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ClassDetailResponse(
        UUID courseId,
        String courseName,
        Integer generation,
        ClassGroup classGroup,
        long studentCount,
        List<StudentInfoResponse> students
) {
}
