package com.example.skala_ium.clazz.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ClassListResponse(
    UUID classId,
    String className,
    Integer generation,
    long studentCount
) {
}
