package com.traveler.bff.dto.service;

import lombok.Data;

@Data
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private String travelPlace;
    private String address;
    private String category;
    private String region;
}