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
        public static OauthLoginDTO of(String provider, String providerId, String email) {
            if (provider == null || providerId == null) {
                throw new WebApiServiceException(WebApiServiceErrorCode.INVALID_OAUTH_REQUEST_PARAM);
            }
            return new OauthLoginDTO(provider, providerId, email);
        }
    }
}
