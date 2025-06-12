package com.traveler.sign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SignDto {
    private Long id;
    private String loginId;
    private String password;
    private String email;
    private String nickname;
    private String gender;
    private List<String> roles;
    private LocalDateTime regDate;
}
