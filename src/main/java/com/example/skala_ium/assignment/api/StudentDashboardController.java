package com.example.skala_ium.assignment.api;

import com.example.skala_ium.assignment.application.StudentDashboardService;
import com.example.skala_ium.assignment.dto.response.StudentAssignmentResponse;
import com.example.skala_ium.assignment.dto.response.dashboard.StudentDashboardResponse;
import com.example.skala_ium.global.auth.security.CustomerDetails;
import com.example.skala_ium.global.response.ApiResponse;
import com.example.skala_ium.global.response.type.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "학생 대시보드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/assignments")
public class StudentDashboardController {

    private final StudentDashboardService studentDashboardService;

    @Operation(summary = "내 과제 종합 대시보드 조회 (학생)")
    @GetMapping("/me/dashboard")
    public ApiResponse<StudentDashboardResponse> getMyDashboard(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        StudentDashboardResponse response = studentDashboardService
            .getStudentDashboard(customerDetails.getAuthenticatable());
        return ApiResponse.success(SuccessType.OK, response);
    }

    @Operation(summary = "특정 강의 내 과제 현황 조회 (학생)")
    @GetMapping("/courses/{classId}/me")
    public ApiResponse<List<StudentAssignmentResponse>> getMyAssignments(
        @PathVariable UUID classId,
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        List<StudentAssignmentResponse> response = studentDashboardService
            .getMyAssignments(customerDetails.getAuthenticatable(), classId);
        return ApiResponse.success(SuccessType.OK, response);
    }
}
