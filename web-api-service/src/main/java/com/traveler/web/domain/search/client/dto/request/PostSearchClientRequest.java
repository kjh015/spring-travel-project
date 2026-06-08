package com.traveler.web.domain.search.client.dto.request;

import com.traveler.web.domain.search.enums.PostSortType;
import lombok.Builder;
import org.springframework.data.domain.Sort;

public final class PostSearchClientRequest {

    private PostSearchClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Builder
    public record SearchDTO(
            String keyword,
            String category,
            String region,
            PostSortType sort,
            Sort.Direction direction,
            Integer size) {}
}
