package com.traveler.web.domain.useractivity.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.useractivity.dto.request.FilterRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.FilterRuleResponse;
import com.traveler.web.domain.useractivity.facade.FilterRuleFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FilterRule", description = "FilterRule API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/api/v1/admin")
public class FilterRuleController {
    private final FilterRuleFacade filterRuleFacade;

    @PostMapping("/log-processes/{logProcessId}/filter-rules")
    public ApiResponse<FilterRuleResponse.CreateDTO> createFilterRule(
            @PathVariable Long logProcessId, @RequestBody FilterRuleRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.createFilterRule(logProcessId, dto));
    }

    @PatchMapping("/filter-rules/{filterRuleId}")
    public ApiResponse<FilterRuleResponse.UpdateDTO> updateFilterRule(
            @PathVariable Long filterRuleId, @RequestBody FilterRuleRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.updateFilterRule(filterRuleId, dto));
    }

    @DeleteMapping("/filter-rules/{filterRuleId}")
    public ApiResponse<FilterRuleResponse.DeleteDTO> deleteFilterRule(@PathVariable Long filterRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.deleteFilterRule(filterRuleId));
    }

    @GetMapping("/log-processes/{logProcessId}/filter-rules")
    public ApiResponse<PageResponse<FilterRuleResponse.ListDTO>> getFilterRules(
            @PathVariable Long logProcessId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.getFilterRules(logProcessId, pageable));
    }

    @GetMapping("/filter-rules/{filterRuleId}")
    public ApiResponse<FilterRuleResponse.DetailDTO> getFilterRule(@PathVariable Long filterRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleFacade.getFilterRule(filterRuleId));
    }
}
