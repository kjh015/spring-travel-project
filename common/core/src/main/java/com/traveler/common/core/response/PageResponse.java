package com.traveler.common.core.response;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.Builder;

@Builder
public record PageResponse<T>(
        List<T> content,
        int currentPage,
        int size,
        long totalElements,
        int totalPages,
        boolean isFirst,
        boolean isLast) {
    public PageResponse {
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
    }
    /**
     * 내부 content의 요소를 변환 함수를 통해 가공한 새로운 PageResponse를 반환합니다.
     */
    public <U> PageResponse<U> map(Function<? super T, U> converter) {
        Objects.requireNonNull(converter, "converter must not be null");
        List<U> mappedContent = this.content.stream().map(converter).toList();

        return PageResponse.<U>builder()
                .content(mappedContent)
                .currentPage(this.currentPage)
                .size(this.size)
                .totalElements(this.totalElements)
                .totalPages(this.totalPages)
                .isFirst(this.isFirst)
                .isLast(this.isLast)
                .build();
    }
}
