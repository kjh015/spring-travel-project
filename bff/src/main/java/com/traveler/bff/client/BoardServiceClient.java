package com.traveler.bff.client;

import com.traveler.bff.config.FormEncoderConfig;
import com.traveler.bff.dto.service.BoardDto;
import com.traveler.bff.dto.service.BoardListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "board", configuration = FormEncoderConfig.class)
public interface BoardServiceClient {
    @GetMapping("/board/search")
    List<BoardListDto> getArticleListBySearch(@RequestParam String keyword, @RequestParam String category, @RequestParam String region,
                                              @RequestParam String sort, @RequestParam String direction, @RequestParam String page);

    @GetMapping("/board/list")
    List<BoardListDto> getArticleList();

    @GetMapping("/board/view")
    BoardDto viewArticle(@RequestParam String no);

    @PostMapping(value = "/board/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> addArticle(@RequestPart("board") String data,
                                      @RequestPart(value = "images", required = false) List<MultipartFile> images);

    @PostMapping(value = "/board/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> editArticle(@RequestPart("board") String data,
                                       @RequestPart(value = "images", required = false) List<MultipartFile> images);

    @PostMapping("/board/remove")
    ResponseEntity<String> removeArticle(@RequestParam String no);

    @PostMapping("/board/list-part")
    List<BoardListDto> getListPart(@RequestParam List<Long> boardIDs);

    @PostMapping("/board/list-member")
    List<BoardListDto> getListByMember(@RequestParam Long memberId);

    @PostMapping("/board/migrate-data")
    ResponseEntity<String> migrateData();
}
