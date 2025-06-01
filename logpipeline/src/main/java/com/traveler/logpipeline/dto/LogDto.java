package com.traveler.logpipeline.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LogDto {
    private Long id;
    private String process;
    private String logJson;
    private String deduplication;
    private String filter;
    private LocalDateTime createdTime;
}
