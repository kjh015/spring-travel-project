package com.traveler.web.domain.member.adaptor;

import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.member.client.AdminClient;
import com.traveler.web.domain.member.client.dto.response.AdminClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminClientAdaptor {
    private final AdminClient adminClient;

    public AdminClientResponse.GrantAdminDTO grantAdminRole(Long memberId) {
        return adminClient.grantAdminRole(memberId).result();
    }

    public AdminClientResponse.DeleteDTO deleteMember(Long memberId) {
        return adminClient.deleteMember(memberId).result();
    }

    public PageResponse<AdminClientResponse.ListDTO> getMembers(Pageable pageable) {
        return adminClient.getMembers(pageable).result();
    }

    public AdminClientResponse.DetailDTO getMember(Long memberId) {
        return adminClient.getMember(memberId).result();
    }
}
