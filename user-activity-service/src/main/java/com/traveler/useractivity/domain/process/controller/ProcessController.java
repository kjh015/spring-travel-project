package com.traveler.useractivity.domain.process.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Process", description = "Process API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/processes")
public class ProcessController {}
