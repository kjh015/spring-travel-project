package com.traveler.web.client.post;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "post-service")
public interface PostServiceClient {}
