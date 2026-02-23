package com.example.skala_ium.class_.domain.entity;

import com.example.skala_ium.class_.domain.ClassGroup;
import com.example.skala_ium.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long id;

    @Column(name = "class_name", nullable = false, length = 100)
    private String className;

    @Column(name = "generation")
    private Integer generation;

    @Enumerated(EnumType.STRING)
    @Column(name = "class_group", nullable = false, length = 20)
    private ClassGroup classGroup;

    @Builder
    public Course(String className, Integer generation, ClassGroup classGroup) {
        this.className = className;
        this.generation = generation;
        this.classGroup = classGroup;
    }
}
