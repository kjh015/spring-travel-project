package com.traveler.comment.controller;

import com.traveler.comment.dto.CommentDto;
import com.traveler.comment.service.CommentService;
import com.traveler.comment.service.CustomCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    //list, add, remove
    @GetMapping("/list")
    public List<CommentDto> getCommentListByBoard(@RequestParam String no){
        return commentService.getCommentList(Long.valueOf(no));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody CommentDto data){
        commentService.addComment(data);
        return ResponseEntity.ok().body("댓글 작성완료");
    }
    @PostMapping("/remove")
    public ResponseEntity<String> removeComment(@RequestParam String commentId){
        commentService.removeComment(Long.valueOf(commentId));
        return ResponseEntity.ok().body("댓글 삭제완료");
    }
    @ExceptionHandler(CustomCommentException.class)
    public ResponseEntity<String> exceptionComment(CustomCommentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
