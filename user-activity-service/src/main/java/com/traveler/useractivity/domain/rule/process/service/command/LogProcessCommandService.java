package com.traveler.useractivity.domain.rule.process.service.command;

import com.traveler.useractivity.domain.rule.process.dto.event.LogProcessEvent;
import com.traveler.useractivity.domain.rule.process.dto.request.LogProcessRequest;
import com.traveler.useractivity.domain.rule.process.dto.response.LogProcessResponse;
import com.traveler.useractivity.domain.rule.process.entity.LogProcess;
import com.traveler.useractivity.domain.rule.process.mapper.LogProcessMapper;
import com.traveler.useractivity.domain.rule.process.repository.LogProcessRepository;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogProcessCommandService {
    private final LogProcessRepository logProcessRepository;
    private final LogProcessMapper logProcessMapper;
    private final ApplicationEventPublisher eventPublisher;

    public LogProcessResponse.CreateDTO createLogProcess(LogProcessRequest.CreateDTO dto) {
        LogProcess logProcess = logProcessMapper.toCreateEntity(dto);

        LogProcess savedLogProcess = logProcessRepository.save(logProcess);

        return logProcessMapper.toCreateDTO(savedLogProcess);
    }

    public LogProcessResponse.UpdateDTO updateLogProcess(Long logProcessId, LogProcessRequest.UpdateDTO dto) {
        LogProcess logProcess = logProcessRepository
                .findById(logProcessId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.LOG_PROCESS_NOT_FOUND));

        logProcess.update(dto.name(), dto.description());

        eventPublisher.publishEvent(new LogProcessEvent.Evict(logProcessId));

        return logProcessMapper.toUpdateDTO(logProcess);
    }

    public LogProcessResponse.DeleteDTO deleteLogProcess(Long logProcessId) {
        LogProcess logProcess = logProcessRepository
                .findById(logProcessId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.LOG_PROCESS_NOT_FOUND));

        logProcess.delete();

        eventPublisher.publishEvent(new LogProcessEvent.Evict(logProcessId));

        return logProcessMapper.toDeleteDTO(logProcess);
    }
}
