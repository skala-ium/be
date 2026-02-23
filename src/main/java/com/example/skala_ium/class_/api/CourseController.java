package com.example.skala_ium.class_.api;

import com.example.skala_ium.class_.application.CourseService;
import com.example.skala_ium.class_.dto.response.CourseDetailResponse;
import com.example.skala_ium.class_.dto.response.CourseListResponse;
import com.example.skala_ium.global.response.ApiResponse;
import com.example.skala_ium.global.response.type.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "반별 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classes")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "강의 목록 조회")
    @GetMapping
    public ApiResponse<List<CourseListResponse>> getCourses() {
        List<CourseListResponse> response = courseService.getAllCourses();
        return ApiResponse.success(SuccessType.OK, response);
    }

    @Operation(summary = "강의 상세 조회")
    @GetMapping("/{courseId}")
    public ApiResponse<CourseDetailResponse> getCourseDetail(@PathVariable Long courseId) {
        CourseDetailResponse response = courseService.getCourseDetail(courseId);
        return ApiResponse.success(SuccessType.OK, response);
    }
}
