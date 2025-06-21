package com.traveler.realtimepopular.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PopularScoreResult {
    private String boardId;
    private String category;
    private String region;
    private long score;
}
