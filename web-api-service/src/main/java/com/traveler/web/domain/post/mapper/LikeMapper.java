package com.traveler.web.domain.post.mapper;

import com.traveler.web.domain.post.client.dto.request.LikeClientRequest;
import com.traveler.web.domain.post.dto.request.LikeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {
    LikeClientRequest.AddDTO toAddClientRequest(LikeRequest.AddDTO dto);
}
