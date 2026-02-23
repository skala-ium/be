package com.example.skala_ium.user.domain.service;

import com.example.skala_ium.global.auth.security.Authenticatable;

public interface UserLoader {

    Authenticatable findByPrincipal(String principal);
}