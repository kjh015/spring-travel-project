package com.traveler.web.domain.member.mapper;

import com.traveler.web.domain.member.client.dto.request.AuthClientRequest;
import com.traveler.web.domain.member.client.dto.response.AuthClientResponse;
import com.traveler.web.domain.member.client.dto.response.KakaoClientResponse;
import com.traveler.web.domain.member.dto.request.AuthRequest;
import com.traveler.web.domain.member.dto.response.AuthResponse;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {
    AuthClientRequest.LoginDTO toClientLoginRequest(AuthRequest.LoginDTO request);

    AuthResponse.LoginDTO toResponseLoginDTO(AuthClientResponse.LoginDTO clientResponse);

    AuthClientRequest.ReissueDTO toClientReissueDTO(String refreshToken);

    @Mapping(target = "provider", constant = "KAKAO")
    @Mapping(target = "providerId", expression = "java(String.valueOf(userInfo.id()))")
    @Mapping(target = "email", expression = "java(extractEmail(userInfo))")
    AuthClientRequest.OauthLoginDTO toOauthLoginDTO(KakaoClientResponse.UserInfoDTO userInfo);

    // Null-Safe 이메일 추출 로직
    default String extractEmail(KakaoClientResponse.UserInfoDTO userInfo) {
        return Optional.ofNullable(userInfo.kakaoAccount())
                .map(KakaoClientResponse.KakaoAccount::email)
                .orElse(null);
    }
}
