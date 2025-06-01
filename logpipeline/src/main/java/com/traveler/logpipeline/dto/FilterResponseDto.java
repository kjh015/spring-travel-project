package com.traveler.logpipeline.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FilterResponseDto {
    private Long id;
    private String name;
    private String tokensJson;
    private boolean isActive;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
