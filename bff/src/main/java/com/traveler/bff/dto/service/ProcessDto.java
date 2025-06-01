package com.traveler.bff.dto.service;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProcessDto {
    private Long id;
    private String name;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
