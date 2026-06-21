package com.traveler.useractivity.domain.process.filterrule.service.command;

import com.traveler.useractivity.domain.process.core.entity.LogProcess;
import com.traveler.useractivity.domain.process.core.repository.LogProcessRepository;
import com.traveler.useractivity.domain.process.filterrule.dto.request.FilterRuleRequest;
import com.traveler.useractivity.domain.process.filterrule.dto.response.FilterRuleResponse;
import com.traveler.useractivity.domain.process.filterrule.engine.SpelExpressionBuilder;
import com.traveler.useractivity.domain.process.filterrule.entity.FilterRule;
import com.traveler.useractivity.domain.process.filterrule.mapper.FilterRuleMapper;
import com.traveler.useractivity.domain.process.filterrule.repository.FilterRuleRepository;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FilterRuleCommandService {
    private final LogProcessRepository logProcessRepository;
    private final FilterRuleRepository filterRuleRepository;
    private final FilterRuleMapper filterRuleMapper;

    public FilterRuleResponse.CreateDTO createFilterRule(Long logProcessId, FilterRuleRequest.CreateDTO dto) {
        LogProcess logProcess = logProcessRepository
                .findById(logProcessId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.LOG_PROCESS_NOT_FOUND));

        // SpEL 표현식 조립
        String expression = SpelExpressionBuilder.build(dto.conditions());

        FilterRule filterRule = filterRuleMapper.toCreateEntity(dto, logProcess, expression);

        FilterRule savedRule = filterRuleRepository.save(filterRule);

        return filterRuleMapper.toCreateDTO(savedRule);
    }

    public FilterRuleResponse.UpdateDTO updateFilterRule(Long filterRuleId, FilterRuleRequest.UpdateDTO dto) {
        FilterRule filterRule = filterRuleRepository
                .findById(filterRuleId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.FILTER_RULE_NOT_FOUND));

        String expression = SpelExpressionBuilder.build(dto.conditions());

        filterRule.update(dto.name(), expression, dto.conditions(), dto.isActive());

        return filterRuleMapper.toUpdateDTO(filterRule);
    }

    public FilterRuleResponse.DeleteDTO deleteFilterRule(Long filterRuleId) {
        FilterRule filterRule = filterRuleRepository
                .findById(filterRuleId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.FILTER_RULE_NOT_FOUND));

        filterRule.delete();

        return filterRuleMapper.toDeleteDTO(filterRule);
    }
}
