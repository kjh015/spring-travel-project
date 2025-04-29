package com.traveler.sign.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResultDto extends SignUpResultDto {
    private String token;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token){
        super(success, code, msg);
        this.token = token;
    }
}
