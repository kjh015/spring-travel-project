package com.traveler.sign.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordDto {
    private String loginId;
    private String curPassword;
    private String newPassword;
}
