package com.example.skala_ium.assignment.api;

import com.example.skala_ium.assignment.application.AssignmentService;
import com.example.skala_ium.assignment.dto.response.AssignmentDetailResponse;
import com.example.skala_ium.assignment.dto.response.AssignmentListResponse;
import com.example.skala_ium.assignment.dto.response.dashboard.DashboardResponse;
import com.example.skala_ium.global.auth.security.CustomerDetails;
import com.example.skala_ium.global.response.ApiResponse;
import com.example.skala_ium.global.response.type.SuccessType;
import com.example.skala_ium.user.domain.entity.Professor;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AssignmentController implements AssignmentControllerDocs {

    private final AssignmentService assignmentService;

    @Override
    @GetMapping("/classes/{classId}/assignments")
    public ApiResponse<Page<AssignmentListResponse>>
    getAssignments(
        @PathVariable UUID classId,
        Pageable pageable
    ) {
        Page<AssignmentListResponse> assignments = assignmentService.getAssignments(classId, pageable);
        return ApiResponse.success(SuccessType.OK, assignments);
    }

    @Override
    @GetMapping("/assignments/{assignmentId}")
    public ApiResponse<AssignmentDetailResponse> getAssignmentDetail(
        @PathVariable UUID assignmentId
    ) {
        AssignmentDetailResponse response = assignmentService.getAssignmentDetail(assignmentId);
        return ApiResponse.success(SuccessType.OK, response);
    }

    @Override
    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> getDashBoardInfo(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        Professor professor = (Professor) customerDetails.getAuthenticatable();
        DashboardResponse response = assignmentService.getDashboard(professor.getId());
        return ApiResponse.success(SuccessType.OK, response);
    }
}
