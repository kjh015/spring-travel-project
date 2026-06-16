package com.traveler.post.domain.comment.service;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.post.domain.comment.dto.request.CommentRequest;
import com.traveler.post.domain.comment.dto.response.CommentResponse;
import com.traveler.post.domain.comment.entity.Comment;
import com.traveler.post.domain.comment.mapper.CommentMapper;
import com.traveler.post.domain.comment.repository.CommentRepository;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.repository.PostRepository;
import com.traveler.post.global.exception.PostServiceException;
import com.traveler.post.global.exception.code.PostServiceErrorCode;
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
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    private final ApplicationEventPublisher eventPublisher;

    public CommentResponse.CreateDTO createComment(Long memberId, CommentRequest.CreateDTO dto) {
        Post post = postRepository
                .findByIdWithLock(dto.postId())
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        post.addComment(dto.star());

        Comment comment = commentMapper.toCreateEntity(dto, post, memberId);
        Comment savedComment = commentRepository.save(comment);

        eventPublisher.publishEvent(commentMapper.toCreatedEvent(savedComment, post));

        return commentMapper.toCreateDTO(savedComment);
    }

    public CommentResponse.UpdateDTO updateComment(Long commentId, Long memberId, CommentRequest.UpdateDTO dto) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMemberId().equals(memberId)) {
            throw new PostServiceException(ErrorCode.FORBIDDEN);
        }

        Post post = postRepository
                .findByIdWithLock(comment.getPost().getId())
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        int oldStar = comment.getStar();
        comment.update(dto.content(), dto.star());
        post.updateComment(oldStar, dto.star());

        eventPublisher.publishEvent(commentMapper.toUpdatedEvent(comment, post));

        return commentMapper.toUpdateDTO(comment);
    }

    public CommentResponse.DeleteDTO deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMemberId().equals(memberId)) {
            throw new PostServiceException(ErrorCode.FORBIDDEN);
        }

        Post post = postRepository
                .findByIdWithLock(comment.getPost().getId())
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        post.removeComment(comment.getStar());
        comment.delete();

        eventPublisher.publishEvent(commentMapper.toDeletedEvent(comment, post));

        return commentMapper.toDeleteDTO(comment);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBatch(List<Long> ids) {
        if (ids.isEmpty()) return;
        commentRepository.hardDeleteCommentsByIds(ids);
    }
}
