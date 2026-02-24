package com.example.skala_ium.global.auth.security;

import java.util.UUID;

public interface Authenticatable {

    UUID getId();

    String getName();

    String getPassword();

    String getRole();

    String getPrincipal(); // 로그인 식별자 (professor=email, student=slack_user_id)
}