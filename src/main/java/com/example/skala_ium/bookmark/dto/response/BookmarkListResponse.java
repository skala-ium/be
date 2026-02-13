package com.example.skala_ium.bookmark.dto.response;

import lombok.Builder;

@Builder
public record BookmarkListResponse(
    Long bookmarkId,
    String name,
    String description
) {

}
