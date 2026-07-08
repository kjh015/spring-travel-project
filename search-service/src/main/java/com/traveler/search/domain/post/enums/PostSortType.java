package com.traveler.search.domain.post.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum PostSortType {
    ACCURACY(null),
    POPULAR("popularityScoreValue"),
    LATEST("createdAt"),
    STAR_AVG("starAvg"),
    VIEW_COUNT("viewCount");
    private final String field;

    public Sort getSort(Sort.Direction direction) {
        if (this == ACCURACY) {
            return Sort.unsorted();
        }
        return Sort.by(direction, this.field);
    }
}
