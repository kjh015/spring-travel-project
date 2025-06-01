package com.traveler.bff.dto.service;

import lombok.Data;

@Data
public class SignDto {
    private String loginId;
    private String password;
    private String email;
    private String nickname;
    private String gender;
    private String role;
}
