package com.example.skala_ium.user.application;

import com.example.skala_ium.user.domain.entity.User;
import com.example.skala_ium.user.dto.response.UserInfoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    public UserInfoResponse getMyInfo(User user) {
        return UserInfoResponse.builder()
            .userId(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }
}
