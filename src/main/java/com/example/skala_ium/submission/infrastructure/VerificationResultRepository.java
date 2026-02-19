package com.example.skala_ium.submission.infrastructure;

import com.example.skala_ium.submission.domain.entity.VerificationResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationResultRepository extends JpaRepository<VerificationResult, Long> {

    List<VerificationResult> findBySubmissionId(Long submissionId);

    void deleteBySubmissionId(Long submissionId);
}
