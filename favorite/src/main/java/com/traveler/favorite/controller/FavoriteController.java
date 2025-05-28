package com.traveler.favorite.controller;

import com.traveler.favorite.dto.FavoriteDto;
import com.traveler.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorite-api")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleFavorite(@RequestBody FavoriteDto data){
        favoriteService.toggleFavorite(data);
        return ResponseEntity.ok().build();
    }
}
