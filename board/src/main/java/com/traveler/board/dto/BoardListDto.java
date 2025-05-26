package com.traveler.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BoardListDto {
    private Long id;
    private String title;
    private String memberNickname;
    private LocalDateTime modifiedDate;
}
