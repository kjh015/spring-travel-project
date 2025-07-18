package com.traveler.bff.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BoardListDto {
    private Long id;
    private String title;
    private Long memberId;
    private LocalDateTime modifiedDate;
}
