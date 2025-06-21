package com.traveler.board.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.board.dto.BoardDto;
import com.traveler.board.dto.BoardListDto;
import com.traveler.board.service.BoardService;
import com.traveler.board.service.CustomBoardException;
import com.traveler.board.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final SearchService searchService;

    Logger logger = LoggerFactory.getLogger(BoardController.class);

    @GetMapping("/search")
    public List<BoardListDto> getArticleListBySearch(@RequestParam String keyword, @RequestParam String category, @RequestParam String region,
                                                     @RequestParam String sort, @RequestParam String direction, @RequestParam String page){
        return boardService.listArticlesBySearch(keyword, category, region, sort, direction, Integer.parseInt(page));
    }
    @GetMapping("/autocomplete")
    public List<String> autocomplete(@RequestParam String keyword) {
        logger.info("---------------------------------------------------------");
        logger.info("keyword: " + keyword);
        List<String> res = new ArrayList<>();
        // 일반 자동완성
        res.addAll(searchService.autocomplete(keyword));
        logger.info("res1: ");
        logger.info(res.toString());
        // 초성 자동완성 (항상 실행)
        String chosung = searchService.getChosung(keyword);
        logger.info("chosung: " + chosung);
        res.addAll(searchService.autocompleteChosung(chosung));
        logger.info("res2: ");
        logger.info(res.toString());
        // 중복제거 후 반환
        return res.stream().distinct().collect(Collectors.toList());
    }


    @GetMapping("/list")
    public List<BoardListDto> getArticleList(){
        return boardService.listArticles();
    }

    @GetMapping("/view")
    public ResponseEntity<BoardDto> viewArticle(@RequestParam String no){
        try{
            return ResponseEntity.ok(boardService.viewArticle(Long.parseLong(no)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addArticle(@RequestPart("board") String board,
                                             @RequestPart(value = "images", required = false) List<MultipartFile> images){
        System.out.println("===== BoardController addArticle 진입 =====");
        System.out.println("board: " + board);
        System.out.println("images: " + images);
        if (images != null) {
            System.out.println("images.size: " + images.size());
        }
        try{
            BoardDto data = new ObjectMapper().readValue(board, BoardDto.class);
            boardService.addArticle(data, images);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/edit")
    public ResponseEntity<String> editArticle(@RequestPart("board") String board,
                                              @RequestPart(value = "images", required = false) List<MultipartFile> images){
        try{
            BoardDto data = new ObjectMapper().readValue(board, BoardDto.class);
            boardService.editArticle(data, images);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeArticle(@RequestParam String no){
        try{
            boardService.removeArticle(Long.parseLong(no));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        // 저장 경로에 맞게 수정
        Path imagePath = Paths.get("C:/develop/project/spring/spring-travel-project/images", filename);
        Resource resource = new UrlResource(imagePath.toUri());
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Type", Files.probeContentType(imagePath))
                .body(resource);
    }

    @PostMapping("/list-part")
    public List<BoardListDto> getListPart(@RequestParam List<Long> boardIDs){
        return boardService.getListPart(boardIDs);
    }
    @PostMapping("/list-member")
    public List<BoardListDto> getListByMember(@RequestParam Long memberId){
        return boardService.getListByMember(memberId);
    }

    @PostMapping("/migrate-data")
    public ResponseEntity<String> migrateData(){
        boardService.migrateAll();
        return ResponseEntity.ok().build();
    }



    @ExceptionHandler(CustomBoardException.class)
    public ResponseEntity<String> exceptionComment(CustomBoardException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> exceptionIO(IOException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IO Error:" + e.getMessage());
    }


}
