package com.traveler.logpipeline.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

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
