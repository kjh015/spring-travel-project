package com.traveler.post.domain.post.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Region {
    GANGWON("강원"),
    GYEONGGI("경기"),
    DAEGU("대구"),
    BUSAN("부산"),
    SEOUL("서울"),
    INCHEON("인천"),
    JEONNAM("전남"),
    JEJU("제주"),
    ETC("기타");

    private final String description;
}
