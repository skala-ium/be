package com.example.skala_ium.assignment.domain.entity;

import com.example.skala_ium.course.domain.entity.Course;
import com.example.skala_ium.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assignment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "slack_post_ts", length = 100)
    private String slackPostTs;

    @Column(name = "topic", length = 50)
    private String topic;

    @Column(name = "week", length = 20)
    private String week;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentRequirement> requirements = new ArrayList<>();

    @Builder
    public Assignment(Course course, String title, String content, LocalDateTime deadline,
                      String slackPostTs, String topic, String week) {
        this.course = course;
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.slackPostTs = slackPostTs;
        this.topic = topic;
        this.week = week;
    }
}
