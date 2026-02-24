package com.example.skala_ium.clazz.api;

import com.example.skala_ium.clazz.application.ClassService;
import com.example.skala_ium.clazz.dto.response.ClassDetailResponse;
import com.example.skala_ium.clazz.dto.response.ClassListResponse;
import com.example.skala_ium.global.response.ApiResponse;
import com.example.skala_ium.global.response.type.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "반별 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classes")
public class ClassController {

    private final ClassService classService;

    @Operation(summary = "강의 목록 조회")
    @GetMapping
    public ApiResponse<List<ClassListResponse>> getClasses() {
        List<ClassListResponse> response = classService.getAllClasses();
        return ApiResponse.success(SuccessType.OK, response);
    }

    @Operation(summary = "강의 상세 조회")
    @GetMapping("/{classId}")
    public ApiResponse<ClassDetailResponse> getCourseDetail(@PathVariable UUID classId) {
        ClassDetailResponse response = classService.getClassDetail(classId);
        return ApiResponse.success(SuccessType.OK, response);
    }
}
