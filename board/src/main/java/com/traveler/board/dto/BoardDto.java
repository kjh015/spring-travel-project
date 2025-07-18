package com.traveler.board.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto {
    private String no;
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
