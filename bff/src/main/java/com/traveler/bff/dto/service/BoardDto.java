package com.traveler.bff.dto.service;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardDto {
    private Long no;
    private String title;
    private String content;
    private Long memberId;
    private String travelPlace;
    private String address;
    private String category;
    private String region;
    private Double ratingAvg;
    private Long viewCount;
    private Long favoriteCount;
    private Long commentCount;
    private List<String> imagePaths;
}
