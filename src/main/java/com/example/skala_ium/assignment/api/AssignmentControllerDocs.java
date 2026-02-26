package com.example.skala_ium.assignment.api;

import com.example.skala_ium.assignment.dto.request.CreateAssignmentRequest;
import com.example.skala_ium.assignment.dto.response.AssignmentDetailResponse;
import com.example.skala_ium.assignment.dto.response.AssignmentListResponse;
import com.example.skala_ium.assignment.dto.response.dashboard.DashboardResponse;
import com.example.skala_ium.global.auth.security.CustomerDetails;
import com.example.skala_ium.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "과제 관련 API")
public interface AssignmentControllerDocs {

    @Operation(summary = "과제 생성 (교수)")
    ApiResponse<?> createAssignment(
        CreateAssignmentRequest request,
        @AuthenticationPrincipal CustomerDetails customerDetails
    );

    @Operation(summary = "특정 강의의 과제 목록 조회 (페이징)")
    ApiResponse<Page<AssignmentListResponse>> getAssignments(
        UUID classesId, Pageable pageable,
        @AuthenticationPrincipal CustomerDetails customerDetails
    );

    @Operation(summary = "과제 상세 조회")
    ApiResponse<AssignmentDetailResponse> getAssignmentDetail(
        UUID assignmentId,
        @AuthenticationPrincipal CustomerDetails customerDetails
    );

    @Operation(summary = "교수용 대시보드 조회")
    ApiResponse<DashboardResponse> getDashBoardInfo(
        @AuthenticationPrincipal CustomerDetails customerDetails
    );
}
