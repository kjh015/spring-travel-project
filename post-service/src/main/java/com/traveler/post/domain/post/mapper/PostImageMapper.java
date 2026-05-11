package com.traveler.post.domain.post.mapper;

import com.traveler.post.domain.post.dto.response.PostImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostImageMapper {

    PostImageResponse.PresignedUrlDTO toPresignedUrlDTO(String url, String imageKey);
}
