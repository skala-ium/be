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
@DiscriminatorValue("STUDENT")
public class Student extends User {

    @Column(name = "major", length = 50)
    private String major;

    @Builder
    public Student(String name, String email, String password, String role, String slackUserId, String major) {
        super(name, email, password, role, slackUserId);
        this.major = major;
    }
}
