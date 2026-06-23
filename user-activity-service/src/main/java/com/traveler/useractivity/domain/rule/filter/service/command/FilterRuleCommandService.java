package com.traveler.useractivity.domain.rule.filter.service.command;

import com.traveler.useractivity.domain.rule.filter.converter.SpelExpressionConverter;
import com.traveler.useractivity.domain.rule.filter.dto.request.FilterRuleRequest;
import com.traveler.useractivity.domain.rule.filter.dto.response.FilterRuleResponse;
import com.traveler.useractivity.domain.rule.filter.entity.FilterRule;
import com.traveler.useractivity.domain.rule.filter.mapper.FilterRuleMapper;
import com.traveler.useractivity.domain.rule.filter.repository.FilterRuleRepository;
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
public class FilterRuleCommandService {
    private final LogProcessRepository logProcessRepository;
    private final FilterRuleRepository filterRuleRepository;
    private final FilterRuleMapper filterRuleMapper;

    public FilterRuleResponse.CreateDTO createFilterRule(Long logProcessId, FilterRuleRequest.CreateDTO dto) {
        LogProcess logProcess = logProcessRepository
                .findById(logProcessId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.LOG_PROCESS_NOT_FOUND));

        // SpEL 표현식 변환
        String expression = SpelExpressionConverter.convert(dto.conditions());

        FilterRule filterRule = filterRuleMapper.toCreateEntity(dto, logProcess, expression);

        FilterRule savedRule = filterRuleRepository.save(filterRule);

        return filterRuleMapper.toCreateDTO(savedRule);
    }

    public FilterRuleResponse.UpdateDTO updateFilterRule(Long filterRuleId, FilterRuleRequest.UpdateDTO dto) {
        FilterRule filterRule = filterRuleRepository
                .findById(filterRuleId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.FILTER_RULE_NOT_FOUND));

        String expression = SpelExpressionConverter.convert(dto.conditions());

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
