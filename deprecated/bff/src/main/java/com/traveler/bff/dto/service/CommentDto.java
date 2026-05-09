package com.traveler.bff.dto.service;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

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
