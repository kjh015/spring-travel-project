package com.traveler.useractivity.domain.process.formatrule.mapper;

import com.traveler.useractivity.domain.process.core.entity.LogProcess;
import com.traveler.useractivity.domain.process.formatrule.dto.request.FormatRuleRequest;
import com.traveler.useractivity.domain.process.formatrule.dto.response.FormatRuleResponse;
import com.traveler.useractivity.domain.process.formatrule.entity.FormatRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FormatRuleMapper {
    @Mapping(source = "process", target = "process")
    FormatRule toCreateEntity(FormatRuleRequest.CreateDTO dto, LogProcess process);

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
