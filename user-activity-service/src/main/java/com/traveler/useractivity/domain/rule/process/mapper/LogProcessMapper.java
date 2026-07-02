package com.traveler.useractivity.domain.rule.process.mapper;

import com.traveler.useractivity.domain.rule.process.dto.request.LogProcessRequest;
import com.traveler.useractivity.domain.rule.process.dto.response.LogProcessResponse;
import com.traveler.useractivity.domain.rule.process.entity.LogProcess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogProcessMapper {
    LogProcess toCreateEntity(LogProcessRequest.CreateDTO dto);

    @Mapping(source = "id", target = "logProcessId")
    LogProcessResponse.CreateDTO toCreateDTO(LogProcess logProcess);

    @Mapping(source = "id", target = "logProcessId")
    LogProcessResponse.UpdateDTO toUpdateDTO(LogProcess logProcess);

    @Mapping(source = "id", target = "logProcessId")
    LogProcessResponse.DeleteDTO toDeleteDTO(LogProcess logProcess);

    @Mapping(source = "id", target = "logProcessId")
    LogProcessResponse.ListDTO toListDTO(LogProcess logProcess);
}
