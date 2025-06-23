package com.traveler.bff.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResultDto {
    private List<BoardListDto> result;
    private Long totalHits;
}
