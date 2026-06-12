package com.traveler.web.domain.member.mapper;

import com.traveler.web.domain.member.client.dto.request.AuthClientRequest;
import com.traveler.web.domain.member.client.dto.response.AuthClientResponse;
import com.traveler.web.domain.member.dto.request.AuthRequest;
import com.traveler.web.domain.member.dto.response.AuthResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {
    AuthClientRequest.LoginDTO toClientLoginRequest(AuthRequest.LoginDTO request);

    AuthResponse.LoginDTO toResponseLoginDTO(AuthClientResponse.LoginDTO clientResponse);

    AuthClientRequest.ReissueDTO toClientReissueDTO(String refreshToken);
}
