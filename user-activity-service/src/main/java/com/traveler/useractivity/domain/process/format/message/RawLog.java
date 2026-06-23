package com.traveler.useractivity.domain.process.format.message;

public record RawLog(
        String remote,
        String host,
        String user,
        String method,
        String path,
        String code,
        String size,
        String referer,
        String agent) {}
