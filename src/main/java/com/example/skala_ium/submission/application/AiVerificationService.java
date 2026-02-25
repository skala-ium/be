package com.example.skala_ium.submission.application;

import com.example.skala_ium.assignment.domain.entity.Assignment;
import com.example.skala_ium.assignment.domain.entity.AssignmentRequirement;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.submission.domain.entity.Submission;
import com.example.skala_ium.submission.domain.entity.VerificationResult;
import com.example.skala_ium.submission.dto.response.VerificationResponse;
import com.example.skala_ium.submission.dto.response.VerificationResultResponse;
import com.example.skala_ium.submission.infrastructure.SubmissionRepository;
import com.example.skala_ium.submission.infrastructure.VerificationResultRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@Transactional
public class AiVerificationService {

    private final SubmissionRepository submissionRepository;
    private final VerificationResultRepository verificationResultRepository;
    private final RestClient openAiRestClient;
    private final ObjectMapper objectMapper;

    public AiVerificationService(
        SubmissionRepository submissionRepository,
        VerificationResultRepository verificationResultRepository,
        @Qualifier("openAiRestClient") RestClient openAiRestClient,
        ObjectMapper objectMapper
    ) {
        this.submissionRepository = submissionRepository;
        this.verificationResultRepository = verificationResultRepository;
        this.openAiRestClient = openAiRestClient;
        this.objectMapper = objectMapper;
    }

    public VerificationResponse verify(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new CustomException(ErrorType.SUBMISSION_NOT_FOUND));

        Assignment assignment = submission.getAssignment();
        List<AssignmentRequirement> requirements = assignment.getRequirements();

        if (requirements.isEmpty()) {
            return VerificationResponse.builder()
                .submissionId(submissionId)
                .results(List.of())
                .overallMet(true)
                .build();
        }

        verificationResultRepository.deleteBySubmissionId(submissionId);

        List<VerificationResult> results = new ArrayList<>();
        boolean overallMet = true;

        for (AssignmentRequirement requirement : requirements) {
            try {
                String prompt = buildPrompt(requirement.getContent(), submission.getContentText());
                String aiResponse = callOpenAi(prompt);
                JsonNode jsonResponse = objectMapper.readTree(aiResponse);

                boolean isMet = jsonResponse.path("isMet").asBoolean(false);
                String feedback = jsonResponse.path("feedback").asText("검증 결과를 확인할 수 없습니다.");

                VerificationResult result = VerificationResult.builder()
                    .submissionId(submissionId)
                    .requirementId(requirement.getId())
                    .isMet(isMet)
                    .feedback(feedback)
                    .verifiedAt(LocalDateTime.now())
                    .build();

                results.add(verificationResultRepository.save(result));

                if (!isMet) {
                    overallMet = false;
                }
            } catch (Exception e) {
                log.error("AI 검증 실패: requirementId={}", requirement.getId(), e);
                throw new CustomException(ErrorType.AI_SERVICE_ERROR);
            }
        }

        submission.updateMetRequirements(overallMet);

        List<VerificationResultResponse> resultResponses = results.stream()
            .map(r -> {
                String content = requirements.stream()
                    .filter(req -> req.getId().equals(r.getRequirementId()))
                    .findFirst()
                    .map(AssignmentRequirement::getContent)
                    .orElse("");

                return VerificationResultResponse.builder()
                    .requirementId(r.getRequirementId())
                    .content(content)
                    .isMet(r.getIsMet())
                    .feedback(r.getFeedback())
                    .build();
            })
            .toList();

        return VerificationResponse.builder()
            .submissionId(submissionId)
            .results(resultResponses)
            .overallMet(overallMet)
            .build();
    }

    @Transactional(readOnly = true)
    public VerificationResponse getVerification(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new CustomException(ErrorType.SUBMISSION_NOT_FOUND));

        List<VerificationResult> results = verificationResultRepository.findBySubmissionId(submissionId);
        if (results.isEmpty()) {
            throw new CustomException(ErrorType.VERIFICATION_NOT_FOUND);
        }

        List<AssignmentRequirement> requirements = submission.getAssignment().getRequirements();

        List<VerificationResultResponse> resultResponses = results.stream()
            .map(r -> {
                String content = requirements.stream()
                    .filter(req -> req.getId().equals(r.getRequirementId()))
                    .findFirst()
                    .map(AssignmentRequirement::getContent)
                    .orElse("");

                return VerificationResultResponse.builder()
                    .requirementId(r.getRequirementId())
                    .content(content)
                    .isMet(r.getIsMet())
                    .feedback(r.getFeedback())
                    .build();
            })
            .toList();

        boolean overallMet = resultResponses.stream().allMatch(VerificationResultResponse::isMet);

        return VerificationResponse.builder()
            .submissionId(submissionId)
            .results(resultResponses)
            .overallMet(overallMet)
            .build();
    }

    private String buildPrompt(String requirementContent, String submissionContent) {
        return String.format("""
            다음은 과제의 요구사항과 학생이 제출한 내용입니다.
            요구사항을 충족하는지 판단하고, JSON 형식으로 응답해주세요.
            
            요구사항: %s
            
            제출 내용: %s
            
            응답 형식 (JSON만 반환):
            {"isMet": true/false, "feedback": "판단 근거 설명"}
            """, requirementContent, submissionContent);
    }

    private String callOpenAi(String prompt) {
        Map<String, Object> requestBody = Map.of(
            "model", "gpt-4o-mini",
            "messages", List.of(
                Map.of("role", "system", "content", "당신은 과제 요구사항 검증 전문가입니다. JSON으로만 응답하세요."),
                Map.of("role", "user", "content", prompt)
            ),
            "temperature", 0.2
        );

        String response = openAiRestClient.post()
            .uri("/chat/completions")
            .body(requestBody)
            .retrieve()
            .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            log.error("OpenAI 응답 파싱 실패", e);
            throw new CustomException(ErrorType.AI_SERVICE_ERROR);
        }
    }
}
