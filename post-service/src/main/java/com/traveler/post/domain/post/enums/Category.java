package com.traveler.post.domain.post.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    FESTIVAL("축제"),
    PERFORMANCE("공연"),
    EVENT("행사"),
    EXPERIENCE("체험"),
    SHOPPING("쇼핑"),
    NATURE("자연"),
    HISTORY("역사"),
    FAMILY("가족"),
    FOOD("음식"),
    ETC("기타");

    private final String description;
}
