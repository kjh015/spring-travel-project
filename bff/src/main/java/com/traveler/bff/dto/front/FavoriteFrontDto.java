package com.traveler.bff.dto.front;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteFrontDto {
    private Long boardId;
    private String memberNickname;
}
