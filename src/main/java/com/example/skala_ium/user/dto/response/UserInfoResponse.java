package com.example.skala_ium.user.dto.response;

import lombok.Builder;

@Builder
public record UserInfoResponse(
    Long userId,
    String name,
    String email,
    String role
) {
}
