package com.traveler.sign.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResultDto extends SignUpResultDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String accessToken, String refreshToken){
        super(success, code, msg);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
