package com.traveler.web.domain.member.client.dto.request;

import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;

public final class AuthClientRequest {
    private AuthClientRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record LoginDTO(String loginId, String password) {}

    public record ReissueDTO(String refreshToken) {}

    public record OauthLoginDTO(String provider, String providerId, String email) {
        public OauthLoginDTO {
            if (provider == null || provider.isBlank() || providerId == null || providerId.isBlank()) {
                throw new WebApiServiceException(WebApiServiceErrorCode.INVALID_OAUTH_REQUEST_PARAM);
            }
        }

        public static OauthLoginDTO of(String provider, String providerId, String email) {
            return new OauthLoginDTO(provider, providerId, email);
        }
    }
}
