package com.traveler.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResultDto {
    private List<BoardListDto> result;
    private Long totalHits;
}
