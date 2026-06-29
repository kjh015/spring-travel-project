package com.traveler.web.domain.useractivity.mapper;

import com.traveler.web.domain.useractivity.client.dto.response.HistoryClientResponse;
import com.traveler.web.domain.useractivity.dto.response.HistoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistoryMapper {
    HistoryResponse.ListDTO toResponseListDTO(HistoryClientResponse.ListDTO listDTO);

    HistoryResponse.DetailDTO toResponseDetailDTO(HistoryClientResponse.DetailDTO clientResponse);
}
