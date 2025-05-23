package com.traveler.board.controller;


import com.traveler.board.dto.BoardDto;
import com.traveler.board.entity.Board;
import com.traveler.board.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    Logger logger = LoggerFactory.getLogger(BoardController.class);

    private List<Board> articleList;


    //signup -> addarticle -> list
    @GetMapping("/list")
    public List<Board> getArticleList(){
        articleList = boardService.listArticles();
        return articleList;
    }

    @GetMapping("/view")
    public ResponseEntity<Board> viewArticle(@RequestParam String no){
        return boardService.viewArticle(Long.parseLong(no))
                .map(ResponseEntity::ok)                    //ResponseEntity.ok(board)
                .orElseGet(() -> ResponseEntity.notFound().build());    // null -> 404
    }

    @PostMapping("/add")
    public ResponseEntity<String> addArticle(@RequestBody BoardDto data){
        try{
            boardService.addArticle(data);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/edit")
    public ResponseEntity<String> editArticle(@RequestBody BoardDto data){
        try{
            boardService.editArticle(data);
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


}
