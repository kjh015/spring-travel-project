package com.traveler.useractivity.domain.rule.format.mapper;

import com.traveler.useractivity.domain.rule.format.dto.request.FormatRuleRequest;
import com.traveler.useractivity.domain.rule.format.dto.response.FormatRuleResponse;
import com.traveler.useractivity.domain.rule.format.entity.FormatRule;
import com.traveler.useractivity.domain.rule.process.entity.LogProcess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FormatRuleMapper {
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.isActive", target = "isActive")
    @Mapping(source = "logProcess", target = "logProcess")
    FormatRule toCreateEntity(FormatRuleRequest.CreateDTO dto, LogProcess logProcess);

    @Mapping(source = "id", target = "formatRuleId")
    FormatRuleResponse.CreateDTO toCreateDTO(FormatRule formatRule);

    @Mapping(source = "id", target = "formatRuleId")
    FormatRuleResponse.UpdateDTO toUpdateDTO(FormatRule formatRule);

    @Mapping(source = "id", target = "formatRuleId")
    FormatRuleResponse.DeleteDTO toDeleteDTO(FormatRule formatRule);

    @Mapping(source = "id", target = "formatRuleId")
    FormatRuleResponse.ListDTO toListDTO(FormatRule formatRule);

    @Mapping(source = "id", target = "formatRuleId")
    FormatRuleResponse.DetailDTO toDetailDTO(FormatRule formatRule);
}
