package com.traveler.logpipeline.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FormatResponseDto {
    private Long id;
    private String name;
    private String defaultJson;
    private String formatJson;
    private boolean isActive;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
