package com.traveler.bff.dto.service;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    private Long no;
    private String content;
    private Long memberId;
    private String rating;
    private LocalDateTime createdTime;
}
