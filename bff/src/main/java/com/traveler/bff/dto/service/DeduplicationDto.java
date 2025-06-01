package com.traveler.bff.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DeduplicationDto {
    private String id;
    private String processId;
    private String name;
    private boolean active;
    private List<Map<String, String>> rows;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
