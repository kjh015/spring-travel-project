package com.traveler.useractivity.domain.rule.filter.service.query;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.rule.filter.dto.response.FilterRuleResponse;
import com.traveler.useractivity.domain.rule.filter.entity.FilterRule;
import com.traveler.useractivity.domain.rule.filter.mapper.FilterRuleMapper;
import com.traveler.useractivity.domain.rule.filter.repository.FilterRuleRepository;
import com.traveler.useractivity.domain.rule.process.repository.LogProcessRepository;
import com.traveler.useractivity.global.exception.UserActivityServiceException;
import com.traveler.useractivity.global.exception.code.UserActivityServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FilterRuleQueryService {
    private final LogProcessRepository logProcessRepository;
    private final FilterRuleRepository filterRuleRepository;
    private final FilterRuleMapper filterRuleMapper;

    public PageResponse<FilterRuleResponse.ListDTO> getFilterRules(Long logProcessId, Pageable pageable) {
        if (!logProcessRepository.existsById(logProcessId)) {
            throw new UserActivityServiceException(UserActivityServiceErrorCode.LOG_PROCESS_NOT_FOUND);
        }

        Page<FilterRule> filterRule = filterRuleRepository.findByLogProcessId(logProcessId, pageable);

        return PageConverter.toPageResponse(filterRule, filterRuleMapper::toListDTO);
    }

    public FilterRuleResponse.DetailDTO getFilterRule(Long filterRuleId) {
        FilterRule filterRule = filterRuleRepository
                .findById(filterRuleId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.FILTER_RULE_NOT_FOUND));

        return filterRuleMapper.toDetailDTO(filterRule);
    }
}
