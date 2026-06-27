package com.traveler.useractivity.domain.history.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.history.dto.response.HistoryResponse;
import com.traveler.useractivity.domain.history.enums.FailStage;
import com.traveler.useractivity.domain.history.enums.HistoryStatus;
import com.traveler.useractivity.domain.history.service.HistoryQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "History", description = "History API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/histories")
public class HistoryController {
    private final HistoryQueryService historyQueryService;

    @GetMapping
    public ApiResponse<PageResponse<HistoryResponse.ListDTO>> getHistories(
            @RequestParam(required = false) HistoryStatus status,
            @RequestParam(required = false) FailStage stage,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, historyQueryService.getHistories(status, stage, pageable));
    }

    @GetMapping("/{historyId}")
    public ApiResponse<HistoryResponse.DetailDTO> getHistory(@PathVariable String historyId) {
        return ApiResponse.onSuccess(SuccessCode.OK, historyQueryService.getHistory(historyId));
    }
}
