package com.traveler.comment.service;

import com.traveler.comment.dto.CommentDto;
import com.traveler.comment.entity.Comment;
import com.traveler.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentDto> getCommentList(Long boardId) throws CustomCommentException{
        List<Comment> commentList = commentRepository.findAllByBoardId(boardId);
        List<CommentDto> dtoList = new ArrayList<>();

        for(Comment comment : commentList){
            CommentDto dto = new CommentDto();
            dto.setContent(comment.getContent());
            dto.setRating(comment.getRating().toString());
            dto.setCreatedTime(comment.getCreatedTime());
            dto.setNickname(comment.getMemberNickname());
            dtoList.add(dto);
        }
        return dtoList;
    }
    public void addComment(CommentDto data) throws CustomCommentException{
        Comment comment = new Comment();
        comment.setRating(Integer.valueOf(data.getRating()));
        comment.setContent(data.getContent());
        comment.setBoardId(Long.valueOf(data.getNo()));
        comment.setMemberNickname(data.getNickname());
        commentRepository.save(comment);

    }
    public void removeComment(Long commentId) throws CustomCommentException{
        commentRepository.deleteById(commentId);
    }
    @Transactional
    public void updateNickname(String prevNickname, String curNickname){
        List<Comment> commentList = commentRepository.findAllByMemberNickname(prevNickname);
        for(Comment comment : commentList){
            comment.setMemberNickname(curNickname);
        }

    }


}
