package com.traveler.web.domain.useractivity.mapper;

import com.traveler.web.domain.useractivity.client.dto.request.FormatRuleClientRequest;
import com.traveler.web.domain.useractivity.client.dto.response.FormatRuleClientResponse;
import com.traveler.web.domain.useractivity.dto.request.FormatRuleRequest;
import com.traveler.web.domain.useractivity.dto.response.FormatRuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FormatRuleMapper {
    FormatRuleClientRequest.CreateDTO toClientCreateDTO(FormatRuleRequest.CreateDTO dto);

    FormatRuleClientRequest.UpdateDTO toClientUpdateDTO(FormatRuleRequest.UpdateDTO dto);

    FormatRuleResponse.CreateDTO toResponseCreateDTO(FormatRuleClientResponse.CreateDTO clientResponse);

    FormatRuleResponse.UpdateDTO toResponseUpdateDTO(FormatRuleClientResponse.UpdateDTO clientResponse);

    FormatRuleResponse.DeleteDTO toResponseDeleteDTO(FormatRuleClientResponse.DeleteDTO clientResponse);

    FormatRuleResponse.ListDTO toResponseListDTO(FormatRuleClientResponse.ListDTO listDTO);

    FormatRuleResponse.DetailDTO toResponseDetailDTO(FormatRuleClientResponse.DetailDTO clientResponse);

    FormatRuleResponse.FieldDTO toResponseFieldDTO(FormatRuleClientResponse.FieldDTO clientResponse);
}
