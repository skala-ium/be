package com.example.skala_ium.assignment.api;

import com.example.skala_ium.assignment.dto.response.AssignmentDetailResponse;
import com.example.skala_ium.assignment.dto.response.AssignmentListResponse;
import com.example.skala_ium.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "과제 관련 API")
public interface AssignmentControllerDocs {

    @Operation(summary = "특정 강의의 과제 목록 조회 (페이징)")
    ApiResponse<Page<AssignmentListResponse>> getAssignments(Long classesId, Pageable pageable);

    @Operation(summary = "과제 상세 조회")
    ApiResponse<AssignmentDetailResponse> getAssignmentDetail(Long assignmentId);
}
