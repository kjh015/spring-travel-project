package com.traveler.logpipeline.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

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
