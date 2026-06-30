package com.traveler.useractivity.domain.rule.format.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.rule.format.dto.request.FormatRuleRequest;
import com.traveler.useractivity.domain.rule.format.dto.response.FormatRuleResponse;
import com.traveler.useractivity.domain.rule.format.service.command.FormatRuleCommandService;
import com.traveler.useractivity.domain.rule.format.service.query.FormatRuleQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Format Rule", description = "Format Rule API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/v1/admin")
public class FormatRuleController {
    private final FormatRuleCommandService formatRuleCommandService;
    private final FormatRuleQueryService formatRuleQueryService;

    @PostMapping("/log-processes/{logProcessId}/format-rules")
    public ApiResponse<FormatRuleResponse.CreateDTO> createFormatRule(
            @PathVariable Long logProcessId, @RequestBody FormatRuleRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleCommandService.createFormatRule(logProcessId, dto));
    }

    @PatchMapping("/format-rules/{formatRuleId}")
    public ApiResponse<FormatRuleResponse.UpdateDTO> updateFormatRule(
            @PathVariable Long formatRuleId, @RequestBody FormatRuleRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleCommandService.updateFormatRule(formatRuleId, dto));
    }

    @DeleteMapping("/format-rules/{formatRuleId}")
    public ApiResponse<FormatRuleResponse.DeleteDTO> deleteFormatRule(@PathVariable Long formatRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleCommandService.deleteFormatRule(formatRuleId));
    }

    @GetMapping("/log-processes/{logProcessId}/format-rules")
    public ApiResponse<PageResponse<FormatRuleResponse.ListDTO>> getFormatRules(
            @PathVariable Long logProcessId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleQueryService.getFormatRules(logProcessId, pageable));
    }

    @GetMapping("/format-rules/{formatRuleId}")
    public ApiResponse<FormatRuleResponse.DetailDTO> getFormatRule(@PathVariable Long formatRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleQueryService.getFormatRule(formatRuleId));
    }

    @GetMapping("/log-processes/{logProcessId}/format-rules/fields")
    public ApiResponse<FormatRuleResponse.FieldDTO> getActiveFormatRuleFields(@PathVariable Long logProcessId) {
        return ApiResponse.onSuccess(SuccessCode.OK, formatRuleQueryService.getActiveFormatRuleFields(logProcessId));
    }
}
