package com.example.skala_ium.submission.domain.entity;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import com.example.skala_ium.global.entity.BaseTimeEntity;
import com.example.skala_ium.user.domain.entity.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "submission")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "content_text", columnDefinition = "TEXT")
    private String contentText;

    @Column(name = "file_url", columnDefinition = "TEXT")
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "slack_thread_ts", length = 100)
    private String slackThreadTs;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;

    @Column(name = "is_met_requirements")
    private Boolean isMetRequirements;

    public void updateMetRequirements(Boolean isMetRequirements) {
        this.isMetRequirements = isMetRequirements;
    }

    @Builder
    public Submission(Assignment assignment, Student student, String contentText,
                      String fileUrl, String fileName, LocalDateTime submittedAt,
                      String slackThreadTs, SubmissionStatus status, Boolean isMetRequirements) {
        this.assignment = assignment;
        this.student = student;
        this.contentText = contentText;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.submittedAt = submittedAt;
        this.slackThreadTs = slackThreadTs;
        this.status = status != null ? status : SubmissionStatus.SUBMITTED;
        this.isMetRequirements = isMetRequirements;
    }
}
