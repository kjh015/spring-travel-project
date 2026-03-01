package com.traveler.board.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResultDto {
    private List<BoardListDto> result;
    private Long totalHits;
}
