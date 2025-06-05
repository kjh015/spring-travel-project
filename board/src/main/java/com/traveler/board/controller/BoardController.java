package com.traveler.board.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.board.dto.BoardDto;
import com.traveler.board.dto.BoardListDto;
import com.traveler.board.service.BoardService;
import com.traveler.board.service.CustomBoardException;
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
import java.util.List;


@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    Logger logger = LoggerFactory.getLogger(BoardController.class);

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

    @ExceptionHandler(CustomBoardException.class)
    public ResponseEntity<String> exceptionComment(CustomBoardException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


}
