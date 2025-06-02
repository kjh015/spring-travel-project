package com.traveler.bff.dto.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardDto {
    private Long no;
    private String title;
    private String content;
    private Long memberId;
    private String travelPlace;
    private String address;
    private String category;
    private String region;
}