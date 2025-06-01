package com.traveler.bff.dto.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResultDto {
    private String accessToken;
    private String refreshToken;
}
