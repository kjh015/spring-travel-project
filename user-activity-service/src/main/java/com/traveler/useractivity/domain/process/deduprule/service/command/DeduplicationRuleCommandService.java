package com.traveler.useractivity.domain.process.deduprule.service.command;

import com.traveler.useractivity.domain.process.core.entity.LogProcess;
import com.traveler.useractivity.domain.process.core.repository.LogProcessRepository;
import com.traveler.useractivity.domain.process.deduprule.dto.request.DeduplicationRuleRequest;
import com.traveler.useractivity.domain.process.deduprule.dto.response.DeduplicationRuleResponse;
import com.traveler.useractivity.domain.process.deduprule.entity.DeduplicationRule;
import com.traveler.useractivity.domain.process.deduprule.mapper.DeduplicationRuleMapper;
import com.traveler.useractivity.domain.process.deduprule.repository.DeduplicationRuleRepository;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeduplicationRuleCommandService {
    private final LogProcessRepository logProcessRepository;
    private final DeduplicationRuleRepository deduplicationRuleRepository;
    private final DeduplicationRuleMapper deduplicationRuleMapper;

    public DeduplicationRuleResponse.CreateDTO createDeduplicationRule(
            Long logProcessId, DeduplicationRuleRequest.CreateDTO dto) {
        LogProcess logProcess = logProcessRepository
                .findById(logProcessId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.LOG_PROCESS_NOT_FOUND));

        DeduplicationRule deduplicationRule = deduplicationRuleMapper.toCreateEntity(dto, logProcess);

        DeduplicationRule savedDeduplicationRule = deduplicationRuleRepository.save(deduplicationRule);

        return deduplicationRuleMapper.toCreateDTO(savedDeduplicationRule);
    }

    public DeduplicationRuleResponse.UpdateDTO updateDeduplicationRule(
            Long deduplicationRuleId, DeduplicationRuleRequest.UpdateDTO dto) {
        DeduplicationRule deduplicationRule = deduplicationRuleRepository
                .findById(deduplicationRuleId)
                .orElseThrow(() -> new UserActivityServiceException(UserActivityServiceErrorCode.DEDUP_RULE_NOT_FOUND));

        deduplicationRule.update(dto.name(), dto.rules(), dto.isActive());

        return deduplicationRuleMapper.toUpdateDTO(deduplicationRule);
    }

    public DeduplicationRuleResponse.DeleteDTO deleteDeduplicationRule(Long deduplicationRuleId) {
        DeduplicationRule deduplicationRule = deduplicationRuleRepository
                .findById(deduplicationRuleId)
                .orElseThrow(() -> new UserActivityServiceException(UserActivityServiceErrorCode.DEDUP_RULE_NOT_FOUND));

        deduplicationRule.delete();
        return deduplicationRuleMapper.toDeleteDTO(deduplicationRule);
    }
}
