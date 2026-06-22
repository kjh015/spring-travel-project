package com.traveler.useractivity.domain.process.deduprule.mapper;

import com.traveler.useractivity.domain.process.core.entity.LogProcess;
import com.traveler.useractivity.domain.process.deduprule.dto.request.DeduplicationRuleRequest;
import com.traveler.useractivity.domain.process.deduprule.dto.response.DeduplicationRuleResponse;
import com.traveler.useractivity.domain.process.deduprule.entity.DeduplicationRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeduplicationRuleMapper {
    @Mapping(source = "logProcess", target = "logProcess")
    DeduplicationRule toCreateEntity(DeduplicationRuleRequest.CreateDTO dto, LogProcess logProcess);

    @Mapping(source = "id", target = "deduplicationRuleId")
    DeduplicationRuleResponse.CreateDTO toCreateDTO(DeduplicationRule deduplicationRule);

    @Mapping(source = "id", target = "deduplicationRuleId")
    DeduplicationRuleResponse.UpdateDTO toUpdateDTO(DeduplicationRule deduplicationRule);

    @Mapping(source = "id", target = "deduplicationRuleId")
    DeduplicationRuleResponse.DeleteDTO toDeleteDTO(DeduplicationRule deduplicationRule);

    @Mapping(source = "id", target = "deduplicationRuleId")
    DeduplicationRuleResponse.ListDTO toListDTO(DeduplicationRule deduplicationRule);

    @Mapping(source = "id", target = "deduplicationRuleId")
    DeduplicationRuleResponse.DetailDTO toDetailDTO(DeduplicationRule deduplicationRule);
}
