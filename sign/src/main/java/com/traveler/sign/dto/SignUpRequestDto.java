package com.traveler.sign.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String loginId;
    private String password;
    private String email;
    private String nickname;
    private String gender;
    private String role;
}
