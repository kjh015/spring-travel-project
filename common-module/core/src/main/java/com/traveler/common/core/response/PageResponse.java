package com.traveler.common.core.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(
        List<T> content,
        int currentPage,
        int size,
        long totalElements,
        int totalPages,
        boolean isFirst,
        boolean isLast) {

}
