package com.traveler.bff.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.bff.client.BoardServiceClient;
import com.traveler.bff.client.SignServiceClient;
import com.traveler.bff.dto.front.BoardFrontDto;
import com.traveler.bff.dto.service.BoardDto;
import com.traveler.bff.dto.service.BoardListDto;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @GetMapping("/search")
    public List<BoardFrontDto> getArticleListBySearch(@RequestParam String keyword, @RequestParam String category, @RequestParam String region,
                                                      @RequestParam String sort, @RequestParam String direction, @RequestParam String page) {
        List<BoardListDto> boardList = boardServiceClient.getArticleListBySearch(keyword, category, region, sort, direction, page);
        Set<Long> IDs = boardList.stream().map(BoardListDto::getMemberId).collect(Collectors.toSet());
        Map<Long, String> nicknameList = signServiceClient.getNicknameList(new ArrayList<>(IDs));
        return boardList.stream().map(board -> BoardFrontDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .memberNickname(nicknameList.get(board.getMemberId()))
                .modifiedDate(board.getModifiedDate()).build()
        ).collect(Collectors.toList());
    }


    @GetMapping("/list")
    public List<BoardFrontDto> getArticleList() {
        List<BoardListDto> boardList = boardServiceClient.getArticleList();
        Set<Long> IDs = boardList.stream().map(BoardListDto::getMemberId).collect(Collectors.toSet());
        Map<Long, String> nicknameList = signServiceClient.getNicknameList(new ArrayList<>(IDs));
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
                .ratingAvg(board.getRatingAvg())
                .commentCount(board.getCommentCount())
                .viewCount(board.getViewCount())
                .favoriteCount(board.getFavoriteCount())
                .imagePaths(board.getImagePaths())
                .build();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addArticle(@RequestPart("board") BoardFrontDto data,
                                        @RequestPart(value = "images", required = false) List<MultipartFile> images) throws JsonProcessingException {
        System.out.println(data);
        System.out.println(images);
        BoardDto board = BoardDto.builder()
                .title(data.getTitle())
                .content(data.getContent())
                .memberId(signServiceClient.getIdByNickname(data.getMemberNickname()))
                .address(data.getAddress())
                .travelPlace(data.getTravelPlace())
                .region(data.getRegion())
                .category(data.getCategory())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String boardJson = mapper.writeValueAsString(board);
        return boardServiceClient.addArticle(boardJson, images);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editArticle(@RequestPart("board") BoardFrontDto data,
                                         @RequestPart(value = "images", required = false) List<MultipartFile> images) throws JsonProcessingException {
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
        ObjectMapper mapper = new ObjectMapper();
        String boardJson = mapper.writeValueAsString(board);
        return boardServiceClient.editArticle(boardJson, images);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeArticle(@RequestParam String no) {
        return boardServiceClient.removeArticle(no);
    }

    @PostMapping("/admin/migrate-data")
    public ResponseEntity<?> migrateData(){
        try{
            boardServiceClient.migrateData();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_SERVER_ERROR).build();
        }
    }
}