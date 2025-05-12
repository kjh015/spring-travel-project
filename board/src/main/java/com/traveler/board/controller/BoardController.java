package com.traveler.board.controller;


import com.traveler.board.entity.Board;
import com.traveler.board.entity.Category;
import com.traveler.board.entity.Region;
import com.traveler.board.entity.TravelPlace;
import com.traveler.board.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PostMapping("/addarticle")
    public Board addArticle(@RequestParam String title, @RequestParam String content, @RequestParam Long memberId){
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setMemberId(memberId);

        TravelPlace tp = new TravelPlace();
        tp.setAddress("tempAddress");
        tp.setName("서울여행");
        Category cg = new Category();
        cg.setName("관광");
        Region rg = new Region();
        rg.setName("서울");
        tp.setCategory(cg);
        tp.setRegion(rg);

        board.setTravelPlace(tp);

        boardService.addArticle(board);

        return board;


    }
//    @GetMapping("/view")
//    public Board viewArticle(@RequestParam String no){
//        logger.info("viewArticle => articleNo: " + no);
//        return boardService.viewArticle(Long.parseLong(no));
//    }
    @GetMapping("/view")
    public Board viewArticle(@RequestParam String no){
        return boardService.viewArticle(Long.parseLong(no));
    }

    @PostMapping("/edit")
    public Board editArticle(@RequestParam String no, @RequestParam String title, @RequestParam String content){
        Board board = new Board();
        board.setId(Long.parseLong(no));
        board.setTitle(title);
        board.setContent(content);
        boardService.editArticle(board);
        logger.info("editArticle => title: " + title);
        logger.info("editArticle => content: " + content);
        return board;
    }

    @PostMapping("/remove")
    public void removeArticle(@RequestParam String no){
        boardService.removeArticle(Long.parseLong(no));
    }


}
