package com.traveler.web.client.search;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "search-service")
public interface SearchServiceClient {}
