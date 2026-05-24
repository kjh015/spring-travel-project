package com.traveler.member.domain.auth.mapper;

import com.traveler.member.domain.auth.dto.AuthTokens;
import com.traveler.member.domain.auth.dto.response.AuthResponse;
import com.traveler.member.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    @Mapping(source = "member.id", target = "loginInfo.memberId")
    @Mapping(source = "member.nickname", target = "loginInfo.nickname")
    AuthResponse.LoginResult toLoginResultDTO(AuthTokens tokens, Member member);
}
