package com.traveler.comment.service;

import com.traveler.comment.dto.CommentDto;
import com.traveler.comment.entity.Comment;
import com.traveler.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

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
    public void addComment(CommentDto data) throws CustomCommentException{
        Comment comment = new Comment();
        comment.setRating(Integer.valueOf(data.getRating()));
        comment.setContent(data.getContent());
        comment.setBoardId(data.getNo());
        comment.setMemberId(data.getMemberId());
        commentRepository.save(comment);

    }
    public void removeComment(Long commentId) throws CustomCommentException{
        commentRepository.deleteById(commentId);
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
