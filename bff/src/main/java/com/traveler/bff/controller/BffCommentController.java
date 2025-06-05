package com.traveler.bff.controller;

import com.traveler.bff.client.CommentServiceClient;
import com.traveler.bff.client.SignServiceClient;
import com.traveler.bff.dto.front.CommentFrontDto;
import com.traveler.bff.dto.service.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class BffCommentController {
    private final CommentServiceClient commentServiceClient;
    private final SignServiceClient signServiceClient;

    @GetMapping("/list")
    public List<CommentFrontDto> getCommentListByBoard(@RequestParam String no) {
        List<CommentDto> commentList = commentServiceClient.getCommentListByBoard(no);
        Set<Long> IDs = commentList.stream().map(CommentDto::getMemberId).collect(Collectors.toSet());
        Map<Long, String> nicknameList = signServiceClient.getNicknameList(new ArrayList<>(IDs));
        return commentList.stream().map(comment -> CommentFrontDto.builder()
                .id(comment.getId())
                .no(comment.getNo())
                .content(comment.getContent())
                .nickname(nicknameList.get(comment.getMemberId()))
                .rating(comment.getRating()).build()
        ).collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody CommentFrontDto data) {
        CommentDto comment = CommentDto.builder()
                .id(data.getId())
                .no(data.getNo())
                .memberId(signServiceClient.getIdByNickname(data.getNickname()))
                .content(data.getContent())
                .rating(data.getRating())
                .build();
        return commentServiceClient.addComment(comment);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeComment(@RequestParam String commentId) {
        return commentServiceClient.removeComment(commentId);
    }
}