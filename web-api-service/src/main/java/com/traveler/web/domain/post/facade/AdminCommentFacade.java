package com.traveler.web.domain.post.facade;

import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.post.client.AdminCommentClient;
import com.traveler.web.domain.post.client.dto.response.AdminCommentClientResponse;
import com.traveler.web.domain.post.dto.response.AdminCommentResponse;
import com.traveler.web.domain.post.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCommentFacade {
    private final AdminCommentClient adminCommentClient;
    private final CommentMapper commentMapper;

    public PageResponse<AdminCommentResponse.ListDTO> getComments(Long postId, Boolean deleted, Pageable pageable) {
        PageResponse<AdminCommentClientResponse.ListDTO> clientResponse =
                adminCommentClient.getComments(postId, deleted, pageable).result();
        return clientResponse.map(commentMapper::toAdminListResponse);
    }

    public AdminCommentResponse.DeleteDTO deleteComment(Long commentId) {
        return commentMapper.toAdminDeleteResponse(
                adminCommentClient.deleteComment(commentId).result());
    }

    public AdminCommentResponse.RestoreDTO restoreComment(Long commentId) {
        return commentMapper.toAdminRestoreResponse(
                adminCommentClient.restoreComment(commentId).result());
    }
}
