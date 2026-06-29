package com.traveler.web.domain.useractivity.mapper;

import com.traveler.web.domain.useractivity.client.dto.request.DedupRuleClientRequest;
import com.traveler.web.domain.useractivity.client.dto.response.DedupRuleClientResponse;
import com.traveler.web.domain.useractivity.dto.request.DedupRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.DedupRuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DedupRuleMapper {
    DedupRuleClientRequest.CreateDTO toClientCreateDTO(DedupRuleRequest.CreateDTO dto);

    DedupRuleClientRequest.UpdateDTO toClientUpdateDTO(DedupRuleRequest.UpdateDTO dto);

    DedupRuleResponse.CreateDTO toResponseCreateDTO(DedupRuleClientResponse.CreateDTO clientResponse);

    DedupRuleResponse.UpdateDTO toResponseUpdateDTO(DedupRuleClientResponse.UpdateDTO clientResponse);

    DedupRuleResponse.DeleteDTO toResponseDeleteDTO(DedupRuleClientResponse.DeleteDTO clientResponse);

    DedupRuleResponse.ListDTO toResponseListDTO(DedupRuleClientResponse.ListDTO listDTO);

    DedupRuleResponse.DetailDTO toResponseDetailDTO(DedupRuleClientResponse.DetailDTO clientResponse);
}
