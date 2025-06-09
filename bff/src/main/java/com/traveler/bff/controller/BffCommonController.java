package com.traveler.bff.controller;

import com.traveler.bff.client.BoardServiceClient;
import com.traveler.bff.client.CommentServiceClient;
import com.traveler.bff.client.FavoriteServiceClient;
import com.traveler.bff.client.SignServiceClient;
import com.traveler.bff.dto.front.BoardFrontDto;
import com.traveler.bff.dto.front.CommentFrontDto;
import com.traveler.bff.dto.service.BoardListDto;
import com.traveler.bff.dto.service.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class BffCommonController {
    private final BoardServiceClient boardServiceClient;
    private final SignServiceClient signServiceClient;
    private final FavoriteServiceClient favoriteServiceClient;
    private final CommentServiceClient commentServiceClient;

    @PostMapping("/my-favorite")
    public List<BoardFrontDto> getFavoriteListByMember(@RequestParam String nickname){
        //nickname -> id
        //id -> fav list -> fav list에서 가져온 boardList를 get요청 -> front 전달
        Long memberId = signServiceClient.getIdByNickname(nickname);
        Set<Long> boardIDs = favoriteServiceClient.getFavoriteList(memberId);
        List<BoardListDto> boardList = boardServiceClient.getListPart(new ArrayList<>(boardIDs));
        return boardList.stream().map(board -> BoardFrontDto.builder()
                .id(board.getId())
                .memberNickname(signServiceClient.getNicknameById(board.getMemberId()))
                .title(board.getTitle())
                .modifiedDate(board.getModifiedDate())
                .build())
                .collect(Collectors.toList());

    }
    @PostMapping("/my-comment")
    public List<CommentFrontDto> getCommentListByMember(@RequestParam String nickname){
        Long memberId = signServiceClient.getIdByNickname(nickname);
        List<CommentDto> comments = commentServiceClient.getCommentListByMember(memberId);
        return comments.stream().map(comment -> CommentFrontDto.builder()
                .no(comment.getNo())
                .id(comment.getId())
                .nickname(signServiceClient.getNicknameById(comment.getMemberId()))
                .createdTime(comment.getCreatedTime())
                .rating(String.valueOf(comment.getRating()))
                .content(comment.getContent())
                .build())
                .collect(Collectors.toList());
    }
    @PostMapping("/my-board")
    public List<BoardFrontDto> getBoardListByMember(@RequestParam String nickname){
        Long memberId = signServiceClient.getIdByNickname(nickname);
        List<BoardListDto> boardList = boardServiceClient.getListByMember(memberId);
        return boardList.stream().map(board -> BoardFrontDto.builder()
                        .id(board.getId())
                        .memberNickname(signServiceClient.getNicknameById(board.getMemberId()))
                        .title(board.getTitle())
                        .modifiedDate(board.getModifiedDate())
                        .build())
                .collect(Collectors.toList());

    }


}
