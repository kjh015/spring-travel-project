package com.traveler.web.domain.member.facade;

import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.member.adaptor.AdminClientAdaptor;
import com.traveler.web.domain.member.client.dto.response.AdminClientResponse;
import com.traveler.web.domain.member.dto.response.AdminResponse;
import com.traveler.web.domain.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFacade {
    private final AdminClientAdaptor adminClientAdaptor;
    private final MemberMapper memberMapper;

    public AdminResponse.GrantAdminDTO grantAdminRole(Long memberId) {
        AdminClientResponse.GrantAdminDTO clientResponse = adminClientAdaptor.grantAdminRole(memberId);
        return memberMapper.toResponseGrantAdminDTO(clientResponse);
    }

    public AdminResponse.DeleteDTO deleteMember(Long memberId) {
        AdminClientResponse.DeleteDTO clientResponse = adminClientAdaptor.deleteMember(memberId);
        return memberMapper.toResponseDeleteDTO(clientResponse);
    }

    public PageResponse<AdminResponse.ListDTO> getMembers(Pageable pageable) {
        PageResponse<AdminClientResponse.ListDTO> clientResponse = adminClientAdaptor.getMembers(pageable);
        return clientResponse.map(memberMapper::toResponseListDTO);
    }

    public AdminResponse.DetailDTO getMember(Long memberId) {
        AdminClientResponse.DetailDTO clientResponse = adminClientAdaptor.getMember(memberId);
        return memberMapper.toResponseDetailDTO(clientResponse);
    }
}
