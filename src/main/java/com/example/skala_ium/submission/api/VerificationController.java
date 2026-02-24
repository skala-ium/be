package com.example.skala_ium.submission.api;

import com.example.skala_ium.global.response.ApiResponse;
import com.example.skala_ium.global.response.type.SuccessType;
import com.example.skala_ium.submission.application.AiVerificationService;
import com.example.skala_ium.submission.dto.response.VerificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AI 검증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/submissions/{submissionId}")
public class VerificationController {

    private final AiVerificationService aiVerificationService;

    @Operation(summary = "AI 요구사항 검증 실행")
    @PostMapping("/verify")
    public ApiResponse<VerificationResponse> verify(@PathVariable UUID submissionId) {
        VerificationResponse response = aiVerificationService.verify(submissionId);
        return ApiResponse.success(SuccessType.OK, response);
    }

    @Operation(summary = "검증 결과 조회")
    @GetMapping("/verification")
    public ApiResponse<VerificationResponse> getVerification(@PathVariable UUID submissionId) {
        VerificationResponse response = aiVerificationService.getVerification(submissionId);
        return ApiResponse.success(SuccessType.OK, response);
    }
}
