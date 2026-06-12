package com.traveler.web.global.security.oauth2.code;

import com.traveler.web.domain.member.client.dto.request.AuthClientRequest;
import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final AuthCodeRepository authCodeRepository;

    public String createAuthCode(String provider, String providerId, String email) {
        String code = UUID.randomUUID().toString();
        AuthClientRequest.OauthLoginDTO oauthLoginReq = AuthClientRequest.OauthLoginDTO.of(provider, providerId, email);

        authCodeRepository.save(code, oauthLoginReq);

        return code;
    }

    public AuthClientRequest.OauthLoginDTO verifyAndConsumeCode(String code) {
        return authCodeRepository
                .findAndDelete(code)
                .orElseThrow(() -> new WebApiServiceException(WebApiServiceErrorCode.INVALID_AUTH_CODE));
    }
}
