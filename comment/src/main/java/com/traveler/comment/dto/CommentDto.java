package com.traveler.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private String no;
    private String content;
    private String nickname;
    private String rating;
    private LocalDateTime createdTime;
}
