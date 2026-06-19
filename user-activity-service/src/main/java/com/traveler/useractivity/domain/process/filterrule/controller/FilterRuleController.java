package com.traveler.useractivity.domain.process.filterrule.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Filter", description = "Filter API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/filters")
public class FilterRuleController {}
