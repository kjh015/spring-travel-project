package com.traveler.bff.dto.front;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentFrontDto {
    private Long id;
    private Long no;
    private String content;
    private String nickname;
    private String rating;
    private LocalDateTime createdTime;
}
