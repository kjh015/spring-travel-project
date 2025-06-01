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
    public ResponseEntity<?> toggleFavorite(@RequestBody FavoriteDto data){
        return ResponseEntity.ok().body(favoriteService.toggleFavorite(data));
    }
    @PostMapping("/exists")
    public ResponseEntity<?> existsFavorite(@RequestBody FavoriteDto data){
        return ResponseEntity.ok().body(favoriteService.existsFavorite(data));
    }

//    @PostMapping("/list")
//    public ResponseEntity<?> getFavoriteList(){
//
//    }
}
