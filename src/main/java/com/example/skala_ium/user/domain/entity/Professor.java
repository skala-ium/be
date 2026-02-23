package com.example.skala_ium.user.domain.entity;

import com.example.skala_ium.global.auth.security.Authenticatable;
import com.example.skala_ium.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professor")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Professor extends BaseTimeEntity implements Authenticatable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "professor_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Builder
    public Professor(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getRole() {
        return "PROFESSOR";
    }

    @Override
    public String getPrincipal() {
        return email;
    }
}
