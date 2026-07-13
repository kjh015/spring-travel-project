package com.traveler.web.domain.post.mapper;

import com.traveler.web.domain.post.client.dto.request.CommentClientRequest;
import com.traveler.web.domain.post.client.dto.response.AdminCommentClientResponse;
import com.traveler.web.domain.post.client.dto.response.CommentClientResponse;
import com.traveler.web.domain.post.dto.request.CommentRequest;
import com.traveler.web.domain.post.dto.response.AdminCommentResponse;
import com.traveler.web.domain.post.dto.response.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    CommentClientRequest.CreateDTO toCreateClientRequest(CommentRequest.CreateDTO dto);

    CommentResponse.CreateDTO toCreateResponse(CommentClientResponse.CreateDTO result);

    CommentClientRequest.UpdateDTO toUpdateClientRequest(CommentRequest.UpdateDTO dto);

    CommentResponse.UpdateDTO toUpdateResponse(CommentClientResponse.UpdateDTO result);

    CommentResponse.DeleteDTO toDeleteResponse(CommentClientResponse.DeleteDTO result);

    // Admin
    AdminCommentResponse.ListDTO toAdminListResponse(AdminCommentClientResponse.ListDTO clientResponse);

    AdminCommentResponse.DeleteDTO toAdminDeleteResponse(AdminCommentClientResponse.DeleteDTO clientResponse);

    AdminCommentResponse.RestoreDTO toAdminRestoreResponse(AdminCommentClientResponse.RestoreDTO clientResponse);
}
