package com.example.skala_ium.user.domain.service;

import com.example.skala_ium.user.domain.entity.User;

public interface UserLoader {

    User findByEmail(String email);
}
