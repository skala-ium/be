package com.example.skala_ium.user.api;

import com.example.skala_ium.global.auth.security.CustomerDetails;
import com.example.skala_ium.global.response.ApiResponse;
import com.example.skala_ium.global.response.type.SuccessType;
import com.example.skala_ium.user.application.UserService;
import com.example.skala_ium.user.dto.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getMyInfo(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        UserInfoResponse response = userService.getMyInfo(customerDetails.getAuthenticatable());
        return ApiResponse.success(SuccessType.OK, response);
    }
}