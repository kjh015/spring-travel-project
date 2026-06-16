package com.traveler.common.core.auth;

import java.util.List;

public record UserContext(Long id, List<String> roles, String accessToken) {
    public UserContext {
        roles = roles == null ? List.of() : List.copyOf(roles);
    }

    public static UserContext of(Long id, List<String> roles, String accessToken) {
        return new UserContext(id, roles, accessToken);
    }
}
