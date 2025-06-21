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
}
