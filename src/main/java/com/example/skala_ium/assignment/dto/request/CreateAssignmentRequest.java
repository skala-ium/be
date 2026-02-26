package com.example.skala_ium.assignment.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateAssignmentRequest(
    @NotNull UUID classId,
    @NotBlank String title,
    String description,
    String topic,
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    LocalDateTime deadline,
    List<String> requirements
) {
}
