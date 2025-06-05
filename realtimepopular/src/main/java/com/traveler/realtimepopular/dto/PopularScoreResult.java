package com.traveler.realtimepopular.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PopularScoreResult {
    private String boardNo;
    private long score;
}
