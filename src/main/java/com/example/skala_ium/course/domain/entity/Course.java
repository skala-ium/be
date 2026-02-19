package com.example.skala_ium.course.domain.entity;

import com.example.skala_ium.global.entity.BaseTimeEntity;
import com.example.skala_ium.user.domain.entity.Professor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Column(name = "semester", length = 20)
    private String semester;

    @Column(name = "class_group", length = 20)
    private String classGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Builder
    public Course(String courseName, String semester, String classGroup, Professor professor) {
        this.courseName = courseName;
        this.semester = semester;
        this.classGroup = classGroup;
        this.professor = professor;
    }
}
