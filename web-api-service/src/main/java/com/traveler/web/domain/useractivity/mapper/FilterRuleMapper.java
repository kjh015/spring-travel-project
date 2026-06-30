package com.traveler.web.domain.useractivity.mapper;

import com.traveler.web.domain.useractivity.client.dto.request.FilterRuleClientRequest;
import com.traveler.web.domain.useractivity.client.dto.response.FilterRuleClientResponse;
import com.traveler.web.domain.useractivity.dto.request.FilterRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.FilterRuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FilterRuleMapper {
    FilterRuleClientRequest.CreateDTO toClientCreateDTO(FilterRuleRequest.CreateDTO dto);

    FilterRuleClientRequest.UpdateDTO toClientUpdateDTO(FilterRuleRequest.UpdateDTO dto);

    FilterRuleResponse.CreateDTO toResponseCreateDTO(FilterRuleClientResponse.CreateDTO clientResponse);

    FilterRuleResponse.UpdateDTO toResponseUpdateDTO(FilterRuleClientResponse.UpdateDTO clientResponse);

    FilterRuleResponse.DeleteDTO toResponseDeleteDTO(FilterRuleClientResponse.DeleteDTO clientResponse);

    FilterRuleResponse.ListDTO toResponseListDTO(FilterRuleClientResponse.ListDTO listDTO);

    FilterRuleResponse.DetailDTO toResponseDetailDTO(FilterRuleClientResponse.DetailDTO clientResponse);
}
