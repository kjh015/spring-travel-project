package com.traveler.sign.dto;

import lombok.Data;

@Data
public class SignInRequestDto {
    private String loginId;
    private String password;
}
