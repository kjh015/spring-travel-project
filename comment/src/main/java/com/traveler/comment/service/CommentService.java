package com.traveler.comment.service;

import com.traveler.comment.dto.CommentDto;
import com.traveler.comment.entity.Comment;
import com.traveler.comment.kafka.service.KafkaService;
import com.traveler.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final KafkaService kafkaService;

    public List<CommentDto> getCommentList(Long boardId) throws CustomCommentException{
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return comments.stream().map(comment -> CommentDto.builder()
                        .no(comment.getBoardId())
                        .id(comment.getId())
                        .memberId(comment.getMemberId())
                        .createdTime(comment.getCreatedTime())
                        .rating(String.valueOf(comment.getRating()))
                        .content(comment.getContent())
                        .build())
                .collect(Collectors.toList());
    }
    @Transactional
    public void addComment(CommentDto data) throws CustomCommentException{
        Comment comment = new Comment();
        comment.setRating(Integer.valueOf(data.getRating()));
        comment.setContent(data.getContent());
        comment.setBoardId(data.getNo());
        comment.setMemberId(data.getMemberId());
        Comment savedComment = commentRepository.save(comment);
        kafkaService.updateRatingAvg(savedComment.getBoardId(), savedComment.getRating(), true);

    }
    @Transactional
    public void removeComment(Long commentId) throws CustomCommentException{
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomCommentException("존재하지 않는 댓글입니다."));
        kafkaService.updateRatingAvg(comment.getBoardId(), comment.getRating(), false);
        commentRepository.delete(comment);
    }

    public List<CommentDto> listCommentByMember(Long memberId){
        List<Comment> comments = commentRepository.findAllByMemberId(memberId);
        return comments.stream().map(comment -> CommentDto.builder()
                .no(comment.getBoardId())
                .id(comment.getId())
                .memberId(comment.getMemberId())
                .createdTime(comment.getCreatedTime())
                .rating(String.valueOf(comment.getRating()))
                .content(comment.getContent())
                .build())
                .collect(Collectors.toList());

    }


}
