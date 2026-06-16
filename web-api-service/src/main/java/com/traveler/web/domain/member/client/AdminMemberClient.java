package com.traveler.web.domain.member.client;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.member.client.dto.response.AdminMemberClientResponse;
import com.traveler.web.global.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "member-service",
        contextId = "AdminClient",
        path = "/v1/admin/members",
        configuration = FeignClientConfig.class)
public interface AdminMemberClient {
    @PatchMapping("/{memberId}/role")
    ApiResponse<AdminMemberClientResponse.GrantAdminDTO> grantAdminRole(@PathVariable Long memberId);

    @DeleteMapping("/{memberId}")
    ApiResponse<AdminMemberClientResponse.DeleteDTO> deleteMember(@PathVariable Long memberId);

    @GetMapping
    ApiResponse<PageResponse<AdminMemberClientResponse.ListDTO>> getMembers(@SpringQueryMap Pageable pageable);

    @GetMapping("/{memberId}")
    ApiResponse<AdminMemberClientResponse.DetailDTO> getMember(@PathVariable Long memberId);
}
