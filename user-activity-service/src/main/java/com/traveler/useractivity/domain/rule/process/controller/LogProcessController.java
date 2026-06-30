package com.traveler.useractivity.domain.rule.process.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.rule.process.dto.request.LogProcessRequest;
import com.traveler.useractivity.domain.rule.process.dto.response.LogProcessResponse;
import com.traveler.useractivity.domain.rule.process.service.command.LogProcessCommandService;
import com.traveler.useractivity.domain.rule.process.service.query.LogProcessQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LogProcess", description = "LogProcess API")
@RestController
@RequiredArgsConstructor
@RequireRole("ROLE_ADMIN")
@RequestMapping("/v1/admin/log-processes")
public class LogProcessController {
    private final LogProcessCommandService logProcessCommandService;
    private final LogProcessQueryService logProcessQueryService;

    @PostMapping
    public ApiResponse<LogProcessResponse.CreateDTO> createLogProcess(@RequestBody LogProcessRequest.CreateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, logProcessCommandService.createLogProcess(dto));
    }

    @PatchMapping("/{logProcessId}")
    public ApiResponse<LogProcessResponse.UpdateDTO> updateLogProcess(
            @PathVariable Long logProcessId, @RequestBody LogProcessRequest.UpdateDTO dto) {
        return ApiResponse.onSuccess(SuccessCode.OK, logProcessCommandService.updateLogProcess(logProcessId, dto));
    }

    @DeleteMapping("/{logProcessId}")
    public ApiResponse<LogProcessResponse.DeleteDTO> deleteLogProcess(@PathVariable Long logProcessId) {
        return ApiResponse.onSuccess(SuccessCode.OK, logProcessCommandService.deleteLogProcess(logProcessId));
    }

    @GetMapping
    public ApiResponse<PageResponse<LogProcessResponse.ListDTO>> getLogProcesses(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, logProcessQueryService.getLogProcesses(pageable));
    }
}
