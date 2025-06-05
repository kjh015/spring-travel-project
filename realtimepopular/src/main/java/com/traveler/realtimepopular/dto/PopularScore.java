package com.traveler.realtimepopular.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PopularScore {
    private long views, likes, comments, score;
}
