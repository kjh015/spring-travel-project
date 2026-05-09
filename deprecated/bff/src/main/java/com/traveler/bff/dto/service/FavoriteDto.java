package com.traveler.bff.dto.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteDto {
    private Long boardId;
    private Long memberId;
}
