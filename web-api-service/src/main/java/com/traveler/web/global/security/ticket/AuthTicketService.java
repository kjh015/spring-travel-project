package com.traveler.web.global.security.ticket;

import com.traveler.web.domain.member.client.dto.request.AuthClientRequest;
import com.traveler.web.global.exception.WebApiServiceException;
import com.traveler.web.global.exception.code.WebApiServiceErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTicketService {

    private final AuthTicketRepository authTicketRepository;

    public String createAuthTicket(String provider, String providerId, String email) {
        String ticket = UUID.randomUUID().toString();
        AuthClientRequest.OauthLoginDTO oauthLoginReq = AuthClientRequest.OauthLoginDTO.of(provider, providerId, email);

        authTicketRepository.save(ticket, oauthLoginReq);

        return ticket;
    }

    public AuthClientRequest.OauthLoginDTO verifyAndConsumeTicket(String ticket) {
        return authTicketRepository
                .findAndDelete(ticket)
                .orElseThrow(() -> new WebApiServiceException(WebApiServiceErrorCode.INVALID_AUTH_TICKET));
    }
}
