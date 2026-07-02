package com.traveler.useractivity.domain.rule.dedup.mapper;

import com.traveler.useractivity.domain.rule.dedup.dto.request.DedupRuleRequest;
import com.traveler.useractivity.domain.rule.dedup.dto.response.DedupRuleResponse;
import com.traveler.useractivity.domain.rule.dedup.entity.DedupRule;
import com.traveler.useractivity.domain.rule.process.entity.LogProcess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DedupRuleMapper {
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.rules", target = "rules")
    @Mapping(source = "dto.isActive", target = "isActive")
    @Mapping(source = "logProcess", target = "logProcess")
    DedupRule toCreateEntity(DedupRuleRequest.CreateDTO dto, LogProcess logProcess);

    @Mapping(source = "id", target = "dedupRuleId")
    DedupRuleResponse.CreateDTO toCreateDTO(DedupRule dedupRule);

    @Mapping(source = "id", target = "dedupRuleId")
    DedupRuleResponse.UpdateDTO toUpdateDTO(DedupRule dedupRule);

    @Mapping(source = "id", target = "dedupRuleId")
    DedupRuleResponse.DeleteDTO toDeleteDTO(DedupRule dedupRule);

    @Mapping(source = "id", target = "dedupRuleId")
    DedupRuleResponse.ListDTO toListDTO(DedupRule dedupRule);

    @Mapping(source = "id", target = "dedupRuleId")
    DedupRuleResponse.DetailDTO toDetailDTO(DedupRule dedupRule);
}
