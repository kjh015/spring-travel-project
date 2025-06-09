package com.traveler.bff.client;

import com.traveler.bff.dto.service.FavoriteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "favorite")
public interface FavoriteServiceClient {
    @PostMapping("/favorite/toggle")
    boolean toggleFavorite(@RequestBody FavoriteDto data);

    @PostMapping("/favorite/exists")
    boolean existsFavorite(@RequestBody FavoriteDto data);

    @PostMapping("/favorite/list")
    Set<Long> getFavoriteList(@RequestParam Long memberId);
}