package com.traveler.post.domain.post.mapper;

import com.traveler.post.domain.post.dto.req.PostReqDTO;
import com.traveler.post.domain.post.entity.TravelPlace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TravelPlaceMapper {

    @Mapping(source = "travelPlace", target = "name")
    TravelPlace toCreateEntity(PostReqDTO.CreateDTO dto);

}
