package com.example.skala_ium.user.application;

import com.example.skala_ium.global.auth.security.Authenticatable;
import com.example.skala_ium.user.dto.response.UserInfoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    public UserInfoResponse getMyInfo(Authenticatable authenticatable) {
        return UserInfoResponse.builder()
            .userId(authenticatable.getId())
            .name(authenticatable.getName())
            .principal(authenticatable.getPrincipal())
            .role(authenticatable.getRole())
            .build();
    }
}