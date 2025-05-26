package com.traveler.board.dto;

import lombok.Data;

@Data
public class BoardDto {
    private String no;
    private String title;
    private String content;
    private String memberNickname;
    private String travelPlace;
    private String address;
    private String category;
    private String region;
}
