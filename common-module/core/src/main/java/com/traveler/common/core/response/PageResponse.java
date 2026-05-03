package com.traveler.common.core.response;

import java.util.List;
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
    /**
     * 내부 content의 요소를 변환 함수를 통해 가공한 새로운 PageResponse를 반환합니다.
     */
    public <U> PageResponse<U> map(Function<? super T, U> converter) {
        List<U> mappedContent = this.content.stream().map(converter).toList();

        return new PageResponse<>(
                mappedContent,
                this.currentPage,
                this.size,
                this.totalElements,
                this.totalPages,
                this.isFirst,
                this.isLast);
    }
}
