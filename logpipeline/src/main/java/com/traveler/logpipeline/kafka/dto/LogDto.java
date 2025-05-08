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
    private String time;
    private String method;
    private String path;
    private String query;
    private String protocol;
    private String code;
    private String size;
    private String referer;
    private String agent;
    private String processId;
    private String timestamp;

    @Override
    public String toString() {
        return String.format("[NGINX] %s %s %s (%s) / query: [%s]", remote, method, path, time, query);
    }
}
