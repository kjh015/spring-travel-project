package com.traveler.bff.dto.front;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchFrontDto {
    private List<BoardFrontDto> result;
    private Long docCount;
}
