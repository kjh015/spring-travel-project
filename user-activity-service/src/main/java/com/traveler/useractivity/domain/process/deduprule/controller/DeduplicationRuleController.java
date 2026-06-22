package com.traveler.useractivity.domain.process.deduprule.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.process.deduprule.dto.request.DeduplicationRuleRequest;
import com.traveler.useractivity.domain.process.deduprule.dto.response.DeduplicationRuleResponse;
import com.traveler.useractivity.domain.process.deduprule.service.command.DeduplicationRuleCommandService;
import com.traveler.useractivity.domain.process.deduprule.service.query.DeduplicationRuleQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Deduplication", description = "Deduplication API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class DeduplicationRuleController {
    private final DeduplicationRuleCommandService deduplicationRuleCommandService;
    private final DeduplicationRuleQueryService deduplicationRuleQueryService;

    @PostMapping("/log-processes/{logProcessId}/deduplication-rules")
    public ApiResponse<DeduplicationRuleResponse.CreateDTO> createDeduplicationRule(
            @PathVariable Long logProcessId, @RequestBody DeduplicationRuleRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(
                SuccessCode.OK, deduplicationRuleCommandService.createDeduplicationRule(logProcessId, dto));
    }

    @PatchMapping("/deduplication-rules/{deduplicationRuleId}")
    public ApiResponse<DeduplicationRuleResponse.UpdateDTO> updateDeduplicationRule(
            @PathVariable Long deduplicationRuleId, @RequestBody DeduplicationRuleRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(
                SuccessCode.OK, deduplicationRuleCommandService.updateDeduplicationRule(deduplicationRuleId, dto));
    }

    @DeleteMapping("/deduplication-rules/{deduplicationRuleId}")
    public ApiResponse<DeduplicationRuleResponse.DeleteDTO> deleteDeduplicationRule(
            @PathVariable Long deduplicationRuleId) {
        return ApiResponse.onSuccess(
                SuccessCode.OK, deduplicationRuleCommandService.deleteDeduplicationRule(deduplicationRuleId));
    }

    @GetMapping("/log-processes/{logProcessId}/deduplication-rules")
    public ApiResponse<PageResponse<DeduplicationRuleResponse.ListDTO>> getDeduplicationRules(
            @PathVariable Long logProcessId,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(
                SuccessCode.OK, deduplicationRuleQueryService.getDeduplicationRules(logProcessId, pageable));
    }

    @GetMapping("/deduplication-rules/{deduplicationRuleId}")
    public ApiResponse<DeduplicationRuleResponse.DetailDTO> getDeduplicationRule(
            @PathVariable Long deduplicationRuleId) {
        return ApiResponse.onSuccess(
                SuccessCode.OK, deduplicationRuleQueryService.getDeduplicationRule(deduplicationRuleId));
    }
}
