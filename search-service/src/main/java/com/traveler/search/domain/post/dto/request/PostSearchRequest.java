package com.traveler.search.domain.post.dto.request;

import com.traveler.search.domain.post.enums.PostSortType;
import lombok.Builder;
import org.springframework.data.domain.Sort;

public class PostSearchRequest {
    @Builder
    public record SearchDTO(
            String keyword,
            String category,
            String region,
            PostSortType sort,
            Sort.Direction direction,
            Integer page,
            Integer size) {}
}
