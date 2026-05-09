package com.traveler.bff.dto.service;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardListDto {
    private Long id;
    private String title;
    private Long memberId;
    private LocalDateTime modifiedDate;
    private String category;
    private String region;
    private Double ratingAvg;
    private Long viewCount;
}
