package com.traveler.web.domain.useractivity.mapper;

import com.traveler.web.domain.useractivity.client.dto.request.LogProcessClientRequest;
import com.traveler.web.domain.useractivity.client.dto.response.LogProcessClientResponse;
import com.traveler.web.domain.useractivity.dto.request.LogProcessRequest;
import com.traveler.web.domain.useractivity.dto.response.LogProcessResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogProcessMapper {

    LogProcessClientRequest.CreateDTO toClientCreateDTO(LogProcessRequest.CreateDTO dto);

    LogProcessClientRequest.UpdateDTO toClientUpdateDTO(LogProcessRequest.UpdateDTO dto);

    LogProcessResponse.CreateDTO toResponseCreateDTO(LogProcessClientResponse.CreateDTO clientResponse);

    LogProcessResponse.UpdateDTO toResponseUpdateDTO(LogProcessClientResponse.UpdateDTO clientResponse);

    LogProcessResponse.DeleteDTO toResponseDeleteDTO(LogProcessClientResponse.DeleteDTO clientResponse);

    LogProcessResponse.ListDTO toResponseListDTO(LogProcessClientResponse.ListDTO listDTO);
}
