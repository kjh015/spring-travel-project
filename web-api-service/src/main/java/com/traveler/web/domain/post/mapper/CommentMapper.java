package com.traveler.web.domain.post.mapper;

import com.traveler.web.domain.post.client.dto.request.CommentClientRequest;
import com.traveler.web.domain.post.client.dto.response.CommentClientResponse;
import com.traveler.web.domain.post.dto.request.CommentRequest;
import com.traveler.web.domain.post.dto.response.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    CommentClientRequest.CreateDTO toCreateClientRequest(CommentRequest.CreateDTO dto);

    CommentResponse.CreateDTO toCreateResponse(CommentClientResponse.CreateDTO response);

    CommentClientRequest.UpdateDTO toUpdateClientRequest(CommentRequest.UpdateDTO dto);

    CommentResponse.UpdateDTO toUpdateResponse(CommentClientResponse.UpdateDTO result);

    CommentResponse.DeleteDTO toDeleteResponse(CommentClientResponse.DeleteDTO result);
}
