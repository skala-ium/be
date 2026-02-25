package com.example.skala_ium.submission.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "verification_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_result_id")
    private Long id;

    @Column(name = "submission_id", nullable = false)
    private Long submissionId;

    @Column(name = "requirement_id", nullable = false)
    private Long requirementId;

    @Column(name = "is_met", nullable = false)
    private Boolean isMet;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Builder
    public VerificationResult(Long submissionId, Long requirementId, Boolean isMet,
                              String feedback, LocalDateTime verifiedAt) {
        this.submissionId = submissionId;
        this.requirementId = requirementId;
        this.isMet = isMet;
        this.feedback = feedback;
        this.verifiedAt = verifiedAt;
    }
}
