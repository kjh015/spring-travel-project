package com.traveler.bff.dto.front;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardFrontDto {
    private Long id;
    private String title;
    private String content;
    private String memberNickname;
    private String travelPlace;
    private String address;
    private String category;
    private String region;
    private LocalDateTime modifiedDate;
}
