package com.traveler.logpipeline.controller;

import com.traveler.logpipeline.entity.Filter;
import com.traveler.logpipeline.service.FilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filter")
public class FilterController {
    private final FilterService filterService;

    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping("/list")
    public List<Filter> getFilterList(){
        return filterService.listFilters();
    }
    @GetMapping("/view")
    public Filter viewFilter(@RequestParam String filterId){
        return filterService.viewFilter(Long.parseLong(filterId));
    }
//    @PostMapping("/add")
//    public ResponseEntity<String> addFilter(){
//        Filter filter = new Filter();
//
//
//    }
//    @PostMapping("/update")
//    public ResponseEntity<String> updateFilter(){
//
//    }
    @PostMapping("/remove")
    public ResponseEntity<String> removeFilter(@RequestParam String filterId){
        filterService.removeFilter(Long.parseLong(filterId));
        return ResponseEntity.ok("필터 삭제 성공");
    }

}
//list, view, add, update, remove
//format이랑 filter에서 boolean 칼럼 만들어서 적용 여부 설정 및 확인
