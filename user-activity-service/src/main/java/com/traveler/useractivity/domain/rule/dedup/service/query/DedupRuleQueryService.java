package com.traveler.useractivity.domain.rule.dedup.service.query;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.useractivity.domain.rule.dedup.dto.response.DedupRuleResponse;
import com.traveler.useractivity.domain.rule.dedup.entity.DedupRule;
import com.traveler.useractivity.domain.rule.dedup.mapper.DedupRuleMapper;
import com.traveler.useractivity.domain.rule.dedup.repository.DedupRuleRepository;
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
public class DedupRuleQueryService {
    private final LogProcessRepository logProcessRepository;
    private final DedupRuleRepository dedupRuleRepository;
    private final DedupRuleMapper dedupRuleMapper;

    public PageResponse<DedupRuleResponse.ListDTO> getDedupRules(Long logProcessId, Pageable pageable) {
        if (!logProcessRepository.existsById(logProcessId)) {
            throw new UserActivityServiceException(UserActivityServiceErrorCode.LOG_PROCESS_NOT_FOUND);
        }

        Page<DedupRule> dedupRule = dedupRuleRepository.findByLogProcessId(logProcessId, pageable);

        return PageConverter.toPageResponse(dedupRule, dedupRuleMapper::toListDTO);
    }

    public DedupRuleResponse.DetailDTO getDedupRule(Long dedupRuleId) {
        DedupRule dedupRule = dedupRuleRepository
                .findById(dedupRuleId)
                .orElseThrow(
                        () -> new UserActivityServiceException(UserActivityServiceErrorCode.FILTER_RULE_NOT_FOUND));

        return dedupRuleMapper.toDetailDTO(dedupRule);
    }
}
