package com.traveler.common.core.response;

import java.util.List;
import lombok.Builder;

@Builder
public record PageResponse<T>(
        List<T> content,
        int currentPage,
        int size,
        long totalElements,
        int totalPages,
        boolean isFirst,
        boolean isLast) {}
