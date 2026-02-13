package com.example.skala_ium.global.auth.jwt.entity;

import lombok.Builder;

@Builder
public record TokenDto(
    String accessToken,
    String refreshToken
) {

}
