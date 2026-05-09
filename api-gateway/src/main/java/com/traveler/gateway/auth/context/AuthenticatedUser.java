package com.traveler.gateway.auth.context;

import java.util.List;

public record AuthenticatedUser(String userId, List<String> roles) {
    public static AuthenticatedUser of(String userId, List<String> roles) {
        return new AuthenticatedUser(userId, roles);
    }
}
