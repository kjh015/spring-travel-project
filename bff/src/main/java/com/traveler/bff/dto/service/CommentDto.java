package com.traveler.bff.dto.service;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long no;
    private String content;
    private Long memberId;
    private String rating;
    private LocalDateTime createdTime;
}
