package com.traveler.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDocument {
    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private String travelPlace;
    private String address;
    private String category;
    private String region;
    private LocalDateTime regDate;
    private LocalDateTime modifiedDate;
    private Double ratingAvg;
    private Long viewCount;
    private Long favoriteCount;
    private Long commentCount;
}
