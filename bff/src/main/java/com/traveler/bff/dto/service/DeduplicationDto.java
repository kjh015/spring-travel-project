package com.traveler.bff.dto.service;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DeduplicationDto {
    private String id;
    private String processId;
    private String name;
    private boolean active;
    private List<RowDto> rows;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @Getter
    @Setter
    public static class RowDto {
        private List<ConditionDto> conditions;
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;
    }

    @Getter @Setter
    public static class ConditionDto {
        private String format;
        private String value;
    }
}
