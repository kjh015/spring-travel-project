package com.traveler.useractivity.domain.rule.dedup.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.rule.dedup.dto.request.DedupRuleRequest;
import com.traveler.useractivity.domain.rule.dedup.dto.response.DedupRuleResponse;
import com.traveler.useractivity.domain.rule.dedup.service.command.DedupRuleCommandService;
import com.traveler.useractivity.domain.rule.dedup.service.query.DedupRuleQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dedup", description = "Dedup API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/v1/admin")
public class DedupRuleController {
    private final DedupRuleCommandService dedupRuleCommandService;
    private final DedupRuleQueryService dedupRuleQueryService;

    @PostMapping("/log-processes/{logProcessId}/dedup-rules")
    public ApiResponse<DedupRuleResponse.CreateDTO> createDedupRule(
            @PathVariable Long logProcessId, @RequestBody DedupRuleRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleCommandService.createDedupRule(logProcessId, dto));
    }

    @PatchMapping("/dedup-rules/{dedupRuleId}")
    public ApiResponse<DedupRuleResponse.UpdateDTO> updateDedupRule(
            @PathVariable Long dedupRuleId, @RequestBody DedupRuleRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleCommandService.updateDedupRule(dedupRuleId, dto));
    }

    @DeleteMapping("/dedup-rules/{dedupRuleId}")
    public ApiResponse<DedupRuleResponse.DeleteDTO> deleteDedupRule(@PathVariable Long dedupRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleCommandService.deleteDedupRule(dedupRuleId));
    }

    @GetMapping("/log-processes/{logProcessId}/dedup-rules")
    public ApiResponse<PageResponse<DedupRuleResponse.ListDTO>> getDedupRules(
            @PathVariable Long logProcessId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleQueryService.getDedupRules(logProcessId, pageable));
    }

    @GetMapping("/dedup-rules/{dedupRuleId}")
    public ApiResponse<DedupRuleResponse.DetailDTO> getDedupRule(@PathVariable Long dedupRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleQueryService.getDedupRule(dedupRuleId));
    }
}
