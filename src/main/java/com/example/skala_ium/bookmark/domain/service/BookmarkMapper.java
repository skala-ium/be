package com.example.skala_ium.bookmark.domain.service;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkInfoResponse;
import com.cliptripbe.feature.bookmark.dto.response.BookmarkListResponse;
import com.cliptripbe.feature.place.dto.response.PlaceListResponse;

public class BookmarkMapper {

    public static BookmarkListResponse mapBookmarkListResponseDto(Bookmark bookmark) {
        return BookmarkListResponse.builder()
            .bookmarkId(bookmark.getId())
            .name(bookmark.getName())
            .description(bookmark.getDescription())
            .build();
    }
}
