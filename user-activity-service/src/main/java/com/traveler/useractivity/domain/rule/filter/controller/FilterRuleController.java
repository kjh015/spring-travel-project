package com.traveler.useractivity.domain.rule.filter.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.rule.filter.dto.request.FilterRuleRequest;
import com.traveler.useractivity.domain.rule.filter.dto.response.FilterRuleResponse;
import com.traveler.useractivity.domain.rule.filter.service.command.FilterRuleCommandService;
import com.traveler.useractivity.domain.rule.filter.service.query.FilterRuleQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Filter", description = "Filter API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class FilterRuleController {
    private final FilterRuleCommandService filterRuleCommandService;
    private final FilterRuleQueryService filterRuleQueryService;

    @PostMapping("/log-processes/{logProcessId}/filter-rules")
    public ApiResponse<FilterRuleResponse.CreateDTO> createFilterRule(
            @PathVariable Long logProcessId, @RequestBody FilterRuleRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleCommandService.createFilterRule(logProcessId, dto));
    }

    @PatchMapping("/filter-rules/{filterRuleId}")
    public ApiResponse<FilterRuleResponse.UpdateDTO> updateFilterRule(
            @PathVariable Long filterRuleId, @RequestBody FilterRuleRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleCommandService.updateFilterRule(filterRuleId, dto));
    }

    @DeleteMapping("/filter-rules/{filterRuleId}")
    public ApiResponse<FilterRuleResponse.DeleteDTO> deleteFilterRule(@PathVariable Long filterRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleCommandService.deleteFilterRule(filterRuleId));
    }

    @GetMapping("/log-processes/{logProcessId}/filter-rules")
    public ApiResponse<PageResponse<FilterRuleResponse.ListDTO>> getFilterRules(
            @PathVariable Long logProcessId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleQueryService.getFilterRules(logProcessId, pageable));
    }

    @GetMapping("/filter-rules/{filterRuleId}")
    public ApiResponse<FilterRuleResponse.DetailDTO> getFilterRule(@PathVariable Long filterRuleId) {
        return ApiResponse.onSuccess(SuccessCode.OK, filterRuleQueryService.getFilterRule(filterRuleId));
    }
}
