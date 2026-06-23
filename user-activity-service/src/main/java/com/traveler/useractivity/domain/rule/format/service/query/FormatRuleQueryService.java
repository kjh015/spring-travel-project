package com.traveler.useractivity.domain.rule.format.service.query;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.rule.format.dto.response.FormatRuleResponse;
import com.traveler.useractivity.domain.rule.format.entity.FormatRule;
import com.traveler.useractivity.domain.rule.format.mapper.FormatRuleMapper;
import com.traveler.useractivity.domain.rule.format.repository.FormatRuleRepository;
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
public class FormatRuleQueryService {
    private final LogProcessRepository logProcessRepository;
    private final FormatRuleRepository formatRuleRepository;
    private final FormatRuleMapper formatRuleMapper;

    public PageResponse<FormatRuleResponse.ListDTO> getFormatRules(Long logProcessId, Pageable pageable) {
        if (!logProcessRepository.existsById(logProcessId)) {
            throw new UserActivityServiceException(UserActivityServiceErrorCode.LOG_PROCESS_NOT_FOUND);
        }

        Page<FormatRule> formatRules = formatRuleRepository.findByLogProcessId(logProcessId, pageable);

        return PageConverter.toPageResponse(formatRules, formatRuleMapper::toListDTO);
    }

    public FormatRuleResponse.DetailDTO getFormatRule(Long formatRuleId) {
        FormatRule formatRule = formatRuleRepository
                .findById(formatRuleId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.FORMAT_RULE_NOT_FOUND));

        return formatRuleMapper.toDetailDTO(formatRule);
    }
}
