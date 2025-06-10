package com.traveler.bff.dto.front;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BoardFrontDto {
    private Long id;
    private String title;
    private String content;
    private String memberNickname;
    private String travelPlace;
    private String address;
    private String category;
    private String region;
    private Double ratingAvg;
    private Long viewCount;
    private Long favoriteCount;
    private Long commentCount;
    private LocalDateTime modifiedDate;
    private List<String> imagePaths;
}
