package com.traveler.logpipeline.controller;

import com.traveler.logpipeline.dto.DeduplicationDto;
import com.traveler.logpipeline.service.CustomLogException;
import com.traveler.logpipeline.service.DeduplicationService;
import com.traveler.logpipeline.service.FormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deduplication")
@RequiredArgsConstructor
public class DeduplicationController {
    private final DeduplicationService deduplicationService;
    private final FormatService formatService;

    @GetMapping("/list")
    public List<DeduplicationDto> getDeduplicationList(@RequestParam String processId){
        return deduplicationService.getDeduplicationList(Long.valueOf(processId));
    }

    @GetMapping("/view")
    public DeduplicationDto viewDeduplication(@RequestParam String deduplicationId){
        return deduplicationService.viewDeduplication(Long.valueOf(deduplicationId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDeduplication(@RequestBody DeduplicationDto data){
        System.out.println("Data: ");
        System.out.println(data);
        deduplicationService.addDeduplication(data);
        return ResponseEntity.ok().body("중복제거 설정 추가 완료");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateDeduplication(@RequestBody DeduplicationDto data){
        System.out.println(data);
        deduplicationService.updateDeduplication(data);
        return ResponseEntity.ok().body("중복제거 설정 수정 완료");
    }
    @PostMapping("/remove")
    public ResponseEntity<?> removeDeduplication(@RequestParam String deduplicationId){
        deduplicationService.removeDeduplication(Long.valueOf(deduplicationId));
        return ResponseEntity.ok().body("중복제거 설정 삭제 완료");
    }
    @GetMapping("/keys")
    public List<String> getFormatList(@RequestParam String processId){
        return formatService.activeFormatKeys(Long.parseLong(processId));
    }

    @ExceptionHandler(CustomLogException.class)
    public ResponseEntity<?> handleException(CustomLogException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
