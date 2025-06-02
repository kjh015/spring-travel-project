package com.traveler.bff.client;

import com.traveler.bff.dto.service.BoardDto;
import com.traveler.bff.dto.service.BoardListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "board")
public interface BoardServiceClient {
    @GetMapping("/board/list")
    List<BoardListDto> getArticleList();

    @GetMapping("/board/view")
    BoardDto viewArticle(@RequestParam String no);

    @PostMapping("/board/add")
    ResponseEntity<String> addArticle(@RequestBody BoardDto data);

    @PostMapping("/board/edit")
    ResponseEntity<String> editArticle(@RequestBody BoardDto data);

    @PostMapping("/board/remove")
    ResponseEntity<String> removeArticle(@RequestParam String no);
}
