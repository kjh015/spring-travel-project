package com.traveler.useractivity.domain.process.deduprule.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Deduplication", description = "Deduplication API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/deduplication")
public class DeduplicationRuleController {}
