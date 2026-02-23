package com.example.skala_ium.global.auth.security;

public interface Authenticatable {

    Long getId();

    String getName();

    String getPassword();

    String getRole();

    String getPrincipal(); // 로그인 식별자 (professor=email, student=slack_user_id)
}