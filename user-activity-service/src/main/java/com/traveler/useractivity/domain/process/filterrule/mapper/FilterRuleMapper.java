package com.traveler.useractivity.domain.process.filterrule.mapper;

import com.traveler.useractivity.domain.process.core.entity.LogProcess;
import com.traveler.useractivity.domain.process.filterrule.dto.request.FilterRuleRequest;
import com.traveler.useractivity.domain.process.filterrule.dto.response.FilterRuleResponse;
import com.traveler.useractivity.domain.process.filterrule.entity.FilterRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FilterRuleMapper {

    @Mapping(source = "logProcess", target = "logProcess")
    @Mapping(source = "expression", target = "expression")
    FilterRule toCreateEntity(FilterRuleRequest.CreateDTO dto, LogProcess logProcess, String expression);

    @Mapping(source = "id", target = "filterRuleId")
    FilterRuleResponse.CreateDTO toCreateDTO(FilterRule savedRule);

    @Mapping(source = "id", target = "filterRuleId")
    FilterRuleResponse.UpdateDTO toUpdateDTO(FilterRule filterRule);

    @Mapping(source = "id", target = "filterRuleId")
    FilterRuleResponse.DeleteDTO toDeleteDTO(FilterRule filterRule);

    @Mapping(source = "id", target = "filterRuleId")
    FilterRuleResponse.ListDTO toListDTO(FilterRule filterRule);

    @Mapping(source = "id", target = "filterRuleId")
    FilterRuleResponse.DetailDTO toDetailDTO(FilterRule filterRule);
}
