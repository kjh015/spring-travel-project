package com.traveler.bff.dto.front;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchFrontDto {
    private List<BoardFrontDto> result;
    private Long docCount;
}
