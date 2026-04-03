package com.traveler.post.domain.comment.service;

import com.traveler.common.core.code.ErrorCode;
import com.traveler.post.domain.comment.dto.req.CommentReqDTO;
import com.traveler.post.domain.comment.dto.res.CommentResDTO;
import com.traveler.post.domain.comment.entity.Comment;
import com.traveler.post.domain.comment.event.CommentEventPublisher;
import com.traveler.post.domain.comment.mapper.CommentMapper;
import com.traveler.post.domain.comment.repository.CommentRepository;
import com.traveler.post.domain.post.entity.Post;
import com.traveler.post.domain.post.repository.PostRepository;
import com.traveler.post.global.code.PostServiceErrorCode;
import com.traveler.post.global.exception.PostServiceException;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    private final CommentEventPublisher commentEventPublisher;

    public CommentResDTO.CreateDTO createComment(Long memberId, CommentReqDTO.CreateDTO dto) {
        Post post = postRepository
                .findById(dto.postId())
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.POST_NOT_FOUND));

        Comment comment = commentMapper.toCreateEntity(dto, post, memberId);

        Comment savedComment = commentRepository.save(comment);
        commentEventPublisher.publishCreated(savedComment);

        return commentMapper.toCreateDTO(savedComment);
    }

    public CommentResDTO.UpdateDTO updateComment(Long commentId, Long memberId, CommentReqDTO.UpdateDTO dto) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMemberId().equals(memberId)) {
            throw new PostServiceException(ErrorCode.FORBIDDEN);
        }

        comment.update(dto.content(), dto.star());
        commentEventPublisher.publishUpdated(comment);

        return commentMapper.toUpdateDTO(comment);
    }

    public CommentResDTO.DeleteDTO deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new PostServiceException(PostServiceErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMemberId().equals(memberId)) {
            throw new PostServiceException(ErrorCode.FORBIDDEN);
        }

        comment.delete();
        commentEventPublisher.publishDeleted(comment);

        return commentMapper.toDeleteDTO(comment);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBatch(List<Long> ids) {
        if (ids.isEmpty()) return;
        commentRepository.hardDeleteCommentsByIds(ids);
    }
}
