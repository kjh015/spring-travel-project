package com.traveler.web.domain.post.mapper;

import com.traveler.web.domain.post.client.dto.request.PostClientRequest;
import com.traveler.web.domain.post.client.dto.response.PostClientResponse;
import com.traveler.web.domain.post.dto.request.PostRequest;
import com.traveler.web.domain.post.dto.response.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostClientRequest.CreateDTO toCreateClientRequest(PostRequest.CreateDTO dto);

    PostResponse.CreateDTO toCreateResponse(PostClientResponse.CreateDTO result);

    PostClientRequest.UpdateDTO toUpdateClientRequest(PostRequest.UpdateDTO dto);

    PostResponse.UpdateDTO toUpdateResponse(PostClientResponse.UpdateDTO result);

    PostResponse.DeleteDTO toDeleteResponse(PostClientResponse.DeleteDTO result);

    PostResponse.PresignedUrlDTO toPresignedUrlResponse(PostClientResponse.PresignedUrlDTO result);
}
