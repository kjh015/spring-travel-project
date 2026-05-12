package com.traveler.member.domain.member.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.member.domain.member.dto.response.AdminResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "Admin API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {
    @PatchMapping("/members/{memberId}/role")
    public ApiResponse<AdminResponse.GrantAdminDTO> grantAdminRole(@PathVariable String memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }

    @DeleteMapping("/members/{memberId}")
    public ApiResponse<AdminResponse.DeleteDTO> deleteMember(@PathVariable String memberId) {
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }
}
