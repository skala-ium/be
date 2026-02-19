package com.example.skala_ium.submission.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSubmissionRequest(
    @NotBlank(message = "제출 내용은 필수입니다.")
    String contentText,

    String fileUrl,

    String fileName
) {
}
