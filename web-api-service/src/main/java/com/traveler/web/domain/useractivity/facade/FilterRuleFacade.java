package com.traveler.web.domain.useractivity.facade;

import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.useractivity.adaptor.FilterRuleClientAdaptor;
import com.traveler.web.domain.useractivity.client.dto.response.FilterRuleClientResponse;
import com.traveler.web.domain.useractivity.dto.request.FilterRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.FilterRuleResponse;
import com.traveler.web.domain.useractivity.mapper.FilterRuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilterRuleFacade {
    private final FilterRuleClientAdaptor filterRuleClientAdaptor;
    private final FilterRuleMapper filterRuleMapper;

    public FilterRuleResponse.CreateDTO createFilterRule(Long logProcessId, FilterRuleRequest.CreateDTO dto) {
        FilterRuleClientResponse.CreateDTO clientResponse =
                filterRuleClientAdaptor.createFilterRule(logProcessId, filterRuleMapper.toClientCreateDTO(dto));
        return filterRuleMapper.toResponseCreateDTO(clientResponse);
    }

    public FilterRuleResponse.UpdateDTO updateFilterRule(Long filterRuleId, FilterRuleRequest.UpdateDTO dto) {
        FilterRuleClientResponse.UpdateDTO clientResponse =
                filterRuleClientAdaptor.updateFilterRule(filterRuleId, filterRuleMapper.toClientUpdateDTO(dto));
        return filterRuleMapper.toResponseUpdateDTO(clientResponse);
    }

    public FilterRuleResponse.DeleteDTO deleteFilterRule(Long filterRuleId) {
        FilterRuleClientResponse.DeleteDTO clientResponse = filterRuleClientAdaptor.deleteFilterRule(filterRuleId);
        return filterRuleMapper.toResponseDeleteDTO(clientResponse);
    }

    public PageResponse<FilterRuleResponse.ListDTO> getFilterRules(Long logProcessId, Pageable pageable) {
        PageResponse<FilterRuleClientResponse.ListDTO> clientResponse =
                filterRuleClientAdaptor.getFilterRules(logProcessId, pageable);
        return clientResponse.map(filterRuleMapper::toResponseListDTO);
    }

    public FilterRuleResponse.DetailDTO getFilterRule(Long filterRuleId) {
        FilterRuleClientResponse.DetailDTO clientResponse = filterRuleClientAdaptor.getFilterRule(filterRuleId);
        return filterRuleMapper.toResponseDetailDTO(clientResponse);
    }
}
