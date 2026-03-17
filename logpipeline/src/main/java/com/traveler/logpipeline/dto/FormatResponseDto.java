package com.traveler.logpipeline.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

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
