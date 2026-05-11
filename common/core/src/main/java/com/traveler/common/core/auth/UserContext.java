package com.traveler.common.core.auth;

import java.util.List;

public record UserContext(Long id, List<String> roles) {
    public static UserContext of(Long id, List<String> roles) {
        return new UserContext(id, roles);
    }
}
