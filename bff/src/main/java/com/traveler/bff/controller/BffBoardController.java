package com.traveler.bff.controller;

import com.traveler.bff.client.BoardServiceClient;
import com.traveler.bff.client.SignServiceClient;
import com.traveler.bff.dto.front.BoardFrontDto;
import com.traveler.bff.dto.service.BoardDto;
import com.traveler.bff.dto.service.BoardListDto;
import lombok.RequiredArgsConstructor;
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
    public BoardDto viewArticle(@RequestParam String no) {
        return boardServiceClient.viewArticle(no);
    }

    @PostMapping("/add")
    public String addArticle(@RequestBody BoardDto data) {
        return boardServiceClient.addArticle(data);
    }

    @PostMapping("/edit")
    public String editArticle(@RequestBody BoardDto data) {
        return boardServiceClient.editArticle(data);
    }

    @PostMapping("/remove")
    public String removeArticle(@RequestParam String no) {
        return boardServiceClient.removeArticle(no);
    }
}