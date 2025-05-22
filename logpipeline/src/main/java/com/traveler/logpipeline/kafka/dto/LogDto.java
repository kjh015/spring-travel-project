package com.traveler.logpipeline.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {
    private String remote;
    private String host;
    private String user;
    private String method;
    private String path;
    private String code;
    private String size;
    private String referer;
    private String agent;
}
