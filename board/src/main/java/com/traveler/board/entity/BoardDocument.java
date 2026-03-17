package com.traveler.board.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDocument {
    private Long id;
    private String title;
    private String titleChosung;
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
