package com.traveler.bff.client;

import com.traveler.bff.dto.service.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "logpipeline")
public interface LogpipelineServiceClient {
    @GetMapping("/process/list")
    List<ProcessDto> getProcessList();

    @PostMapping("/process/add")
    void addProcess(@RequestParam String name);

    @PostMapping("/process/update")
    void updateProcess(@RequestParam String processId, @RequestParam String name);

    @PostMapping("/process/remove")
    void removeProcess(@RequestParam String processId);

    //format
    @GetMapping("/format/list")
    List<FormatResponseDto> getFormatList(@RequestParam String processId);

    @GetMapping("/format/view")
    FormatResponseDto viewFormat(@RequestParam String formatId);

    @PostMapping("/format/add")
    ResponseEntity<String> addFormat(@RequestParam String processId, @RequestParam String name, @RequestParam String active, @RequestBody FormatRequestDto formatData);

    @PostMapping("/format/update")
    ResponseEntity<String> updateFormat(@RequestParam String formatId, @RequestParam String name, @RequestParam String active, @RequestBody FormatRequestDto formatData);

    @PostMapping("/format/remove")
    ResponseEntity<String> removeFormat(@RequestParam String formatId);

    //filter
    @GetMapping("/filter/list")
    List<FilterResponseDto> getFilterList(@RequestParam String processId);

    @GetMapping("/filter/view")
    FilterResponseDto viewFilter(@RequestParam String filterId);

    @GetMapping("/filter/keys")
    List<String> getFormatFieldsF(@RequestParam String processId);

    @PostMapping("/filter/add")
    ResponseEntity<String> addFilter(@RequestParam String processId, @RequestParam String name, @RequestParam String active, @RequestBody FilterRequestDto data);

    @PostMapping("/filter/update")
    ResponseEntity<String> updateFilter(@RequestParam String filterId, @RequestParam String name, @RequestParam String active, @RequestBody FilterRequestDto data);

    @PostMapping("/filter/remove")
    ResponseEntity<String> removeFilter(@RequestParam String filterId);

    //dedup
    @GetMapping("/deduplication/list")
    List<DeduplicationDto> getDeduplicationList(@RequestParam String processId);

    @GetMapping("/deduplication/view")
    DeduplicationDto viewDeduplication(@RequestParam String deduplicationId);

    @PostMapping("/deduplication/add")
    ResponseEntity<String> addDeduplication(@RequestBody DeduplicationDto data);

    @PostMapping("/deduplication/update")
    ResponseEntity<String> updateDeduplication(@RequestBody DeduplicationDto data);

    @PostMapping("/deduplication/remove")
    ResponseEntity<String> removeDeduplication(@RequestParam String deduplicationId);

    @GetMapping("/deduplication/keys")
    List<String> getFormatFieldsD(@RequestParam String processId);

    //db
    @GetMapping("/log-db/success")
    List<LogDto> listSuccessLogs();

    @GetMapping("/log-db/fail-filter")
    List<LogDto> listFailLogs();

    @GetMapping("/log-db/fail-deduplication")
    List<LogDto> listFailDdpLogs();
}
