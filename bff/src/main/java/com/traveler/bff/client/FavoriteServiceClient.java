package com.traveler.bff.client;

import com.traveler.bff.dto.service.FavoriteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "favorite")
public interface FavoriteServiceClient {
    @PostMapping("/favorite-api/toggle")
    Object toggleFavorite(@RequestBody FavoriteDto data);

    @PostMapping("/favorite-api/exists")
    Object existsFavorite(@RequestBody FavoriteDto data);
}