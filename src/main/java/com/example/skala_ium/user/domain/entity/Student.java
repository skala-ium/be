package com.example.skala_ium.user.domain.entity;

import com.example.skala_ium.clazz.domain.entity.Clazz;
import com.example.skala_ium.global.auth.security.Authenticatable;
import com.example.skala_ium.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "student")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends BaseTimeEntity implements Authenticatable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "student_id")
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "slack_user_id", unique = true, nullable = false, length = 50)
    private String slackUserId;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "major", length = 50)
    private String major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Clazz clazz;

    @Builder
    public Student(String name, String slackUserId, String password, String major, Clazz clazz) {
        this.name = name;
        this.slackUserId = slackUserId;
        this.password = password;
        this.major = major;
        this.clazz = clazz;
    }

    @Override
    public UUID getId() {
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
        return "STUDENT";
    }

    @Override
    public String getPrincipal() {
        return slackUserId;
    }
}
