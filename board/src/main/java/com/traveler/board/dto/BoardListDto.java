package com.traveler.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BoardListDto {
    private Long id;
    private String title;
    private Long memberId;
    private LocalDateTime modifiedDate;
    private String category;
    private String region;
    private Double ratingAvg;
    private Long viewCount;

    public BoardListDto(Long id, String title, Long memberId, LocalDateTime modifiedDate,
                        String category, String region, Long viewCount, Double ratingAvg) {
        this.id = id;
        this.title = title;
        this.memberId = memberId;
        this.modifiedDate = modifiedDate;
        this.category = category;
        this.region = region;
        this.viewCount = viewCount;
        this.ratingAvg = ratingAvg;
    }
}
