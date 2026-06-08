package com.traveler.member.domain.member.mapper;

import com.traveler.member.domain.member.dto.request.MemberRequest;
import com.traveler.member.domain.member.dto.response.AdminResponse;
import com.traveler.member.domain.member.dto.response.MemberResponse;
import com.traveler.member.domain.member.entity.Member;
import com.traveler.member.domain.member.enums.AvailabilityType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    @Mapping(source = "encodedPassword", target = "password")
    Member toCreateEntity(MemberRequest.SignUpDTO dto, String encodedPassword);

    @Mapping(source = "id", target = "memberId")
    MemberResponse.SignUpDTO toSignUpDTO(Member member);

    @Mapping(source = "id", target = "memberId")
    MemberResponse.WithdrawDTO toWithdrawDTO(Member member);

    @Mapping(source = "id", target = "memberId")
    MemberResponse.UpdateDTO toUpdateDTO(Member member);

    @Mapping(source = "id", target = "memberId")
    MemberResponse.UpdatePasswordDTO toUpdatePasswordDTO(Member member);

    @Mapping(source = "member.id", target = "memberId")
    MemberResponse.ProfileDTO toProfileDTO(Member member, Integer age);

    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "member.roleTypes", target = "roles")
    MemberResponse.MyProfileDTO toMyProfileDTO(Member member, Integer age);

    MemberResponse.AvailabilityDTO AvailabilityDTO(boolean isAvailable, String value, AvailabilityType reason);

    // Admin
    @Mapping(source = "id", target = "memberId")
    @Mapping(source = "member.roleTypes", target = "roles")
    AdminResponse.GrantAdminDTO toGrantAdminDTO(Member member);

    @Mapping(source = "id", target = "memberId")
    AdminResponse.DeleteDTO toDeleteDTO(Member member);

    @Mapping(source = "id", target = "memberId")
    @Mapping(source = "member.roleTypes", target = "roles")
    AdminResponse.ListDTO toListDTO(Member member);

    @Mapping(source = "id", target = "memberId")
    @Mapping(source = "member.roleTypes", target = "roles")
    AdminResponse.DetailDTO toDetailDTO(Member member);
}
