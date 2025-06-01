package com.traveler.bff.controller;

import com.traveler.bff.client.CommentServiceClient;
import com.traveler.bff.dto.service.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class BffCommentController {
    private final CommentServiceClient commentServiceClient;

    @GetMapping("/list")
    public List<CommentDto> getCommentListByBoard(@RequestParam String no) {
        return commentServiceClient.getCommentListByBoard(no);
    }

    @PostMapping("/add")
    public String addComment(@RequestBody CommentDto data) {
        return commentServiceClient.addComment(data);
    }

    @PostMapping("/remove")
    public String removeComment(@RequestParam String commentId) {
        return commentServiceClient.removeComment(commentId);
    }
}