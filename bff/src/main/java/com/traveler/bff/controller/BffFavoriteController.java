package com.traveler.bff.controller;

import com.traveler.bff.client.FavoriteServiceClient;
import com.traveler.bff.dto.service.FavoriteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class BffFavoriteController {
    private final FavoriteServiceClient favoriteServiceClient;

    @PostMapping("/toggle")
    public Object toggleFavorite(@RequestBody FavoriteDto data) {
        return favoriteServiceClient.toggleFavorite(data);
    }

    @PostMapping("/exists")
    public Object existsFavorite(@RequestBody FavoriteDto data) {
        return favoriteServiceClient.existsFavorite(data);
    }
}