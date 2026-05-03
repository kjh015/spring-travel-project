package com.traveler.web.domain.search.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostSortType {
    ACCURACY,
    POPULAR,
    LATEST,
    STAR_AVG,
    VIEW_COUNT;
}
