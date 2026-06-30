package com.traveler.web.domain.useractivity.controller;

import com.traveler.common.api.auth.annotation.RequireRole;
import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.useractivity.dto.response.HistoryResponse;
import com.traveler.web.domain.useractivity.enums.FailStage;
import com.traveler.web.domain.useractivity.enums.HistoryStatus;
import com.traveler.web.domain.useractivity.facade.HistoryFacade;
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
@RequireRole("ROLE_ADMIN")
@RequestMapping("/api/v1/admin/histories")
public class HistoryController {
    private final HistoryFacade historyFacade;

    @GetMapping
    public ApiResponse<PageResponse<HistoryResponse.ListDTO>> getHistories(
            @RequestParam(required = false) HistoryStatus status,
            @RequestParam(required = false) FailStage stage,
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable) {
        return ApiResponse.onSuccess(SuccessCode.OK, historyFacade.getHistories(status, stage, pageable));
    }

    @GetMapping("/{historyId}")
    public ApiResponse<HistoryResponse.DetailDTO> getHistory(@PathVariable String historyId) {
        return ApiResponse.onSuccess(SuccessCode.OK, historyFacade.getHistory(historyId));
    }
}
