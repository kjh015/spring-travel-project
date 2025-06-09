package com.traveler.bff.client;

import com.traveler.bff.dto.service.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "comment")
public interface CommentServiceClient {
    @GetMapping("/comment/list")
    List<CommentDto> getCommentListByBoard(@RequestParam String no);

    @PostMapping("/comment/add")
    ResponseEntity<String> addComment(@RequestBody CommentDto data);

    @PostMapping("/comment/remove")
    ResponseEntity<String> removeComment(@RequestParam String commentId);

    @PostMapping("/comment/list-member")
    List<CommentDto> getCommentListByMember(@RequestParam Long memberId);
}