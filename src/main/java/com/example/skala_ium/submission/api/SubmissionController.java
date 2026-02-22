package com.example.skala_ium.submission.api;

import com.example.skala_ium.global.auth.security.CustomerDetails;
import com.example.skala_ium.global.response.ApiResponse;
import com.example.skala_ium.global.response.exception.CustomException;
import com.example.skala_ium.global.response.type.ErrorType;
import com.example.skala_ium.global.response.type.SuccessType;
import com.example.skala_ium.submission.application.SubmissionService;
import com.example.skala_ium.submission.dto.request.CreateSubmissionRequest;
import com.example.skala_ium.submission.dto.response.MySubmissionResponse;
import com.example.skala_ium.submission.dto.response.SubmissionResponse;
import com.example.skala_ium.submission.dto.response.SubmissionStatusResponse;
import com.example.skala_ium.user.domain.entity.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "제출 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(summary = "과제 제출")
    @PostMapping("/assignments/{assignmentId}/submissions")
    public ApiResponse<?> submitAssignment(
        @PathVariable Long assignmentId,
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @Valid @RequestBody CreateSubmissionRequest request
    ) {
        Student student = requireStudent(customerDetails);
        submissionService.submitAssignment(assignmentId, student, request);
        return ApiResponse.success(SuccessType.CREATED);
    }

    @Operation(summary = "과제별 제출 목록 조회 (교수용)")
    @GetMapping("/assignments/{assignmentId}/submissions")
    public ApiResponse<List<SubmissionResponse>> getSubmissions(
        @PathVariable Long assignmentId
    ) {
        List<SubmissionResponse> response = submissionService.getSubmissionsByAssignment(assignmentId);
        return ApiResponse.success(SuccessType.OK, response);
    }

    @Operation(summary = "과제별 제출 현황 조회 (전체 학생 O/X)")
    @GetMapping("/assignments/{assignmentId}/submissions/status")
    public ApiResponse<List<SubmissionStatusResponse>> getSubmissionStatus(
        @PathVariable Long assignmentId,
        @RequestParam Long courseId
    ) {
        List<SubmissionStatusResponse> response = submissionService.getSubmissionStatus(assignmentId, courseId);
        return ApiResponse.success(SuccessType.OK, response);
    }

    @Operation(summary = "내 제출 조회 (특정 과제)")
    @GetMapping("/assignments/{assignmentId}/submissions/me")
    public ApiResponse<SubmissionResponse> getMySubmissionForAssignment(
        @PathVariable Long assignmentId,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        Student student = requireStudent(customerDetails);
        SubmissionResponse response = submissionService
            .getMySubmissionForAssignment(assignmentId, student);
        return ApiResponse.success(SuccessType.OK, response);
    }

    @Operation(summary = "내 전체 제출 목록 조회 (학생용)")
    @GetMapping("/submissions/me")
    public ApiResponse<List<MySubmissionResponse>> getMySubmissions(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        Student student = requireStudent(customerDetails);
        List<MySubmissionResponse> response = submissionService.getMySubmissions(student);
        return ApiResponse.success(SuccessType.OK, response);
    }

    private Student requireStudent(CustomerDetails customerDetails) {
        if (customerDetails.getAuthenticatable() instanceof Student student) {
            return student;
        }
        throw new CustomException(ErrorType.INVALID_ROLE);
    }
}