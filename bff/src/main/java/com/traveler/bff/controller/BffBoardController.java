package com.traveler.bff.controller;

import com.traveler.bff.client.BoardServiceClient;
import com.traveler.bff.client.SignServiceClient;
import com.traveler.bff.dto.front.BoardFrontDto;
import com.traveler.bff.dto.service.BoardDto;
import com.traveler.bff.dto.service.BoardListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BffBoardController {
    private final BoardServiceClient boardServiceClient;
    private final SignServiceClient signServiceClient;

    @GetMapping("/list")
    public List<BoardFrontDto> getArticleList() {
        List<BoardListDto> boardList = boardServiceClient.getArticleList();
        Set<Long> IDs = boardList.stream().map(BoardListDto::getMemberId).collect(Collectors.toSet());
        Map<Long, String> nicknameList = signServiceClient.getNicknameList(IDs);
        return boardList.stream().map(board -> BoardFrontDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .memberNickname(nicknameList.get(board.getMemberId()))
                .modifiedDate(board.getModifiedDate()).build()
        ).collect(Collectors.toList());
    }

    @GetMapping("/view")
    public BoardFrontDto viewArticle(@RequestParam String no) {
        BoardDto board = boardServiceClient.viewArticle(no);
        return BoardFrontDto.builder()
                .id(board.getNo())
                .title(board.getTitle())
                .content(board.getContent())
                .region(board.getRegion())
                .address(board.getAddress())
                .travelPlace(board.getTravelPlace())
                .category(board.getCategory())
                .memberNickname(signServiceClient.getNicknameById(board.getMemberId()))
                .build();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addArticle(@RequestBody BoardFrontDto data) {
        BoardDto board = BoardDto.builder()
                .title(data.getTitle())
                .content(data.getContent())
                .memberId(signServiceClient.getIdByNickname(data.getMemberNickname()))
                .address(data.getAddress())
                .travelPlace(data.getTravelPlace())
                .region(data.getRegion())
                .category(data.getCategory())
                .build();
        return boardServiceClient.addArticle(board);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editArticle(@RequestBody BoardFrontDto data) {
        BoardDto board = BoardDto.builder()
                .no(data.getId())
                .title(data.getTitle())
                .content(data.getContent())
                .memberId(signServiceClient.getIdByNickname(data.getMemberNickname()))
                .address(data.getAddress())
                .travelPlace(data.getTravelPlace())
                .region(data.getRegion())
                .category(data.getCategory())
                .build();
        return boardServiceClient.editArticle(board);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeArticle(@RequestParam String no) {
        return boardServiceClient.removeArticle(no);
    }
}