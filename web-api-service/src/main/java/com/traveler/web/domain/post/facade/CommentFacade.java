package com.traveler.web.domain.post.facade;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.web.domain.post.client.CommentClient;
import com.traveler.web.domain.post.client.dto.response.CommentClientResponse;
import com.traveler.web.domain.post.dto.request.CommentRequest;
import com.traveler.web.domain.post.dto.response.CommentResponse;
import com.traveler.web.domain.post.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentFacade {
    private final CommentClient commentClient;
    private final CommentMapper commentMapper;

    public CommentResponse.CreateDTO createComment(CommentRequest.CreateDTO dto) {
        ApiResponse<CommentClientResponse.CreateDTO> response =
                commentClient.createComment(commentMapper.toCreateClientRequest(dto));
        return commentMapper.toCreateResponse(response.result());
    }

    public CommentResponse.UpdateDTO updateComment(Long commentId, CommentRequest.UpdateDTO dto) {
        ApiResponse<CommentClientResponse.UpdateDTO> response =
                commentClient.updateComment(commentId, commentMapper.toUpdateClientRequest(dto));
        return commentMapper.toUpdateResponse(response.result());
    }

    public CommentResponse.DeleteDTO deleteComment(Long commentId) {
        ApiResponse<CommentClientResponse.DeleteDTO> response = commentClient.deleteComment(commentId);
        return commentMapper.toDeleteResponse(response.result());
    }
}
