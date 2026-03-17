package com.traveler.common.api.converter;

import com.traveler.common.core.response.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public class PageConverter {
    /**
     * Page&lt;T&gt;를 PageResponse&lt;T&gt;로 변환합니다.
     *
     * <p>주로 DTO객체를 페이징하기 위해 사용합니다.
     *
     * @param pageData Page 객체
     * @param <T> 데이터의 타입
     * @return 페이징 정보가 담긴 PageResponse
     */
    public static <T> PageResponse<T> toPageResponse(Page<T> pageData) {
        return PageResponse.<T>builder()
                .content(pageData.getContent())
                .currentPage(pageData.getNumber() + 1)
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .isFirst(pageData.isFirst())
                .isLast(pageData.isLast())
                .build();
    }

    /**
     * Page&lt;Entity&gt;를 PageResponse&lt;DTO&gt;로 변환합니다. Page&lt;Entity&gt;의 내용을 Converter를 통해 DTO 타입으로
     * 바꾼 뒤 PageResponse&lt;DTO&gt;을 생성합니다.
     *
     * @param pageData Page 객체
     * @param converter 리스트 내부의 각 요소를 변환할 함수 (사용 예: DomainConverter::toDto)
     * @param <E> (Entity) 원본 데이터 타입
     * @param <R> (Result) 변환 후 반환할 데이터 타입
     * @return 변환된 데이터 리스트와 페이징 정보가 담긴 PageResponse
     */
    public static <E, R> PageResponse<R> toPageResponse(Page<E> pageData, Function<E, R> converter) {
        List<R> content = pageData.getContent().stream()
                .map(converter)
                .toList();

        return PageResponse.<R>builder()
                .content(content)
                .currentPage(pageData.getNumber() + 1)
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .isFirst(pageData.isFirst())
                .isLast(pageData.isLast())
                .build();
    }
}
