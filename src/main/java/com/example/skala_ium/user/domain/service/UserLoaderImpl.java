package com.example.skala_ium.user.domain.service;

import com.example.skala_ium.global.auth.security.Authenticatable;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.user.infrastructure.ProfessorRepository;
import com.example.skala_ium.user.infrastructure.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLoaderImpl implements UserLoader {

    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    @Override
    public Authenticatable findByPrincipal(String principal) {
        return professorRepository.findByEmail(principal)
            .map(professor -> (Authenticatable) professor)
            .or(() -> studentRepository.findBySlackUserId(principal)
                .map(student -> (Authenticatable) student))
            .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
    }
}