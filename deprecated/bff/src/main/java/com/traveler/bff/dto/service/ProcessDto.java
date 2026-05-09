package com.traveler.bff.dto.service;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessDto {
    private Long id;
    private String name;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
