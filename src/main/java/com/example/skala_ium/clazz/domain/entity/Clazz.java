package com.example.skala_ium.clazz.domain.entity;

import com.example.skala_ium.clazz.domain.ClassGroup;
import com.example.skala_ium.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clazz extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "class_id")
    private UUID id;

    @Column(name = "class_name", nullable = false, length = 100)
    private String className;

    @Column(name = "slack_channel_id", unique = true, length = 50)
    private String slackChannelId;

    @Column(name = "generation")
    private Integer generation;

    @Enumerated(EnumType.STRING)
    @Column(name = "class_group", nullable = false, length = 20)
    private ClassGroup classGroup;

    @Builder
    public Clazz(String className, Integer generation, ClassGroup classGroup) {
        this.className = className;
        this.generation = generation;
        this.classGroup = classGroup;
    }
}
