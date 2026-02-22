package com.example.skala_ium.global.auth.security;

import com.example.skala_ium.user.infrastructure.ProfessorRepository;
import com.example.skala_ium.user.infrastructure.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerDetailsService implements UserDetailsService {

    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        // Professor는 email로 식별, Student는 slackUserId로 식별
        // email 형식이면 Professor로 먼저 조회, 그 다음 Student slackUserId로 조회
        return professorRepository.findByEmail(principal)
            .map(professor -> (Authenticatable) professor)
            .or(() -> studentRepository.findBySlackUserId(principal)
                .map(student -> (Authenticatable) student))
            .map(CustomerDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException(
                "사용자를 찾을 수 없습니다: " + principal));
    }
}