package com.traveler.post.domain.comment.service;

import com.traveler.post.domain.comment.dto.req.CommentReqDTO;
import com.traveler.post.domain.comment.dto.res.CommentResDTO;
import com.traveler.post.domain.comment.entity.Comment;
import com.traveler.post.domain.comment.mapper.CommentMapper;
import com.traveler.post.domain.comment.repository.CommentRepository;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.repository.PostRepository;
import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CommentResDTO.CreateDTO createComment(CommentReqDTO.CreateDTO dto) {
        Post post = postRepository
                .findById(dto.postId())
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        Comment comment = commentMapper.toCreateEntity(dto, post);

        Comment savedComment = commentRepository.save(comment);
        eventPublisher.publishEvent(commentMapper.toCreatedMsgDTO(savedComment));

        return commentMapper.toCreateDTO(savedComment);
    }

    public CommentResDTO.UpdateDTO updateComment(Long commentId, CommentReqDTO.UpdateDTO dto) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.COMMENT_NOT_FOUND));

        comment.update(dto.content(), dto.star());
        eventPublisher.publishEvent(commentMapper.toUpdatedMsgDTO(comment));

        return commentMapper.toUpdateDTO(comment);
    }

    public CommentResDTO.DeleteDTO deleteComment(Long commentId) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.COMMENT_NOT_FOUND));

        comment.delete();
        eventPublisher.publishEvent(commentMapper.toDeletedMsgDTO(comment));

        return commentMapper.toDeleteDTO(comment);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBatch(List<Long> ids) {
        if (ids.isEmpty()) return;
        commentRepository.hardDeleteCommentsByIds(ids);
    }
}
