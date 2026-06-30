package com.traveler.useractivity.domain.rule.process.service.query;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.rule.process.dto.response.LogProcessResponse;
import com.traveler.useractivity.domain.rule.process.entity.LogProcess;
import com.traveler.useractivity.domain.rule.process.mapper.LogProcessMapper;
import com.traveler.useractivity.domain.rule.process.repository.LogProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogProcessQueryService {
    private final LogProcessRepository logProcessRepository;
    private final LogProcessMapper logProcessMapper;

    public PageResponse<LogProcessResponse.ListDTO> getLogProcesses(Pageable pageable) {
        Page<LogProcess> LogProcesses = logProcessRepository.findAll(pageable);

        return PageConverter.toPageResponse(LogProcesses, logProcessMapper::toListDTO);
    }
}
