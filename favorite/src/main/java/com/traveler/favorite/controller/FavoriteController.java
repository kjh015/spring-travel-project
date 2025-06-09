package com.traveler.favorite.controller;

import com.traveler.favorite.dto.FavoriteDto;
import com.traveler.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/favorite")
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

    @PostMapping("/list")
    public Set<Long> getFavoriteList(@RequestParam Long memberId){
        return favoriteService.listFavorite(memberId);
    }
}
