package com.traveler.bff.dto.service;

import lombok.Data;

@Data
public class SignInRequestDto {
    private String loginId;
    private String password;
}
