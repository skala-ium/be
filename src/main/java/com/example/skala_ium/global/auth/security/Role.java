package com.example.skala_ium.global.auth.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    STUDENT("ROLE_USER"),
    PROFESSOR("ROLE_USER");

    private final String role;
}
