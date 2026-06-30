package com.traveler.web.domain.useractivity.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.useractivity.dto.request.DedupRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.DedupRuleResponse;
import com.traveler.web.domain.useractivity.facade.DedupRuleFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DedupRule", description = "DedupRule API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/api/v1/admin")
public class DedupRuleController {
    private final DedupRuleFacade dedupRuleFacade;

    @PostMapping("/log-processes/{logProcessId}/dedup-rules")
    public ApiResponse<DedupRuleResponse.CreateDTO> createDedupRule(
            @PathVariable Long logProcessId, @RequestBody DedupRuleRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.createDedupRule(logProcessId, dto));
    }

    @PatchMapping("/dedup-rules/{dedupRuleId}")
    public ApiResponse<DedupRuleResponse.UpdateDTO> updateDedupRule(
            @PathVariable Long dedupRuleId, @RequestBody DedupRuleRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.updateDedupRule(dedupRuleId, dto));
    }

    @DeleteMapping("/dedup-rules/{dedupRuleId}")
    public ApiResponse<DedupRuleResponse.DeleteDTO> deleteDedupRule(@PathVariable Long dedupRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.deleteDedupRule(dedupRuleId));
    }

    @GetMapping("/log-processes/{logProcessId}/dedup-rules")
    public ApiResponse<PageResponse<DedupRuleResponse.ListDTO>> getDedupRules(
            @PathVariable Long logProcessId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.getDedupRules(logProcessId, pageable));
    }

    @GetMapping("/dedup-rules/{dedupRuleId}")
    public ApiResponse<DedupRuleResponse.DetailDTO> getDedupRule(@PathVariable Long dedupRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, dedupRuleFacade.getDedupRule(dedupRuleId));
    }
}
