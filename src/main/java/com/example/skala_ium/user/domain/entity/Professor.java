package com.example.skala_ium.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("PROFESSOR")
public class Professor extends User {

    @Column(name = "department", length = 50)
    private String department;

    @Builder
    public Professor(String name, String email, String password, String role, String slackUserId, String department) {
        super(name, email, password, role, slackUserId);
        this.department = department;
    }
}
