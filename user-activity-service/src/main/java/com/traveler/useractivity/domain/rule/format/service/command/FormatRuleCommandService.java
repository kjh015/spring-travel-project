package com.traveler.useractivity.domain.rule.format.service.command;

import com.traveler.useractivity.domain.rule.format.dto.request.FormatRuleRequest;
import com.traveler.useractivity.domain.rule.format.dto.response.FormatRuleResponse;
import com.traveler.useractivity.domain.rule.format.entity.FormatRule;
import com.traveler.useractivity.domain.rule.format.mapper.FormatRuleMapper;
import com.traveler.useractivity.domain.rule.format.repository.FormatRuleRepository;
import com.traveler.useractivity.domain.rule.process.entity.LogProcess;
import com.traveler.useractivity.domain.rule.process.repository.LogProcessRepository;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FormatRuleCommandService {
    private final LogProcessRepository logProcessRepository;
    private final FormatRuleRepository formatRuleRepository;
    private final FormatRuleMapper formatRuleMapper;

    public FormatRuleResponse.CreateDTO createFormatRule(Long logProcessId, FormatRuleRequest.CreateDTO dto) {
        LogProcess logProcess = logProcessRepository
                .findById(logProcessId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.LOG_PROCESS_NOT_FOUND));

        FormatRule formatRule = formatRuleMapper.toCreateEntity(dto, logProcess);

        FormatRule savedFormatRule = formatRuleRepository.save(formatRule);

        return formatRuleMapper.toCreateDTO(savedFormatRule);
    }

    public FormatRuleResponse.UpdateDTO updateFormatRule(Long formatRuleId, FormatRuleRequest.UpdateDTO dto) {
        FormatRule formatRule = formatRuleRepository
                .findById(formatRuleId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.FORMAT_RULE_NOT_FOUND));

        formatRule.update(dto.name(), dto.defaultValues(), dto.fieldMappings(), dto.isActive());

        return formatRuleMapper.toUpdateDTO(formatRule);
    }

    public FormatRuleResponse.DeleteDTO deleteFormatRule(Long formatRuleId) {
        FormatRule formatRule = formatRuleRepository
                .findById(formatRuleId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.FORMAT_RULE_NOT_FOUND));
        formatRule.delete();
        return formatRuleMapper.toDeleteDTO(formatRule);
    }
}
