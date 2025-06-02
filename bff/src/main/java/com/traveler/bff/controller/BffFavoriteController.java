package com.traveler.bff.controller;

import com.traveler.bff.client.FavoriteServiceClient;
import com.traveler.bff.client.SignServiceClient;
import com.traveler.bff.dto.front.FavoriteFrontDto;
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
    private final SignServiceClient signServiceClient;

    @PostMapping("/toggle")
    public boolean toggleFavorite(@RequestBody FavoriteFrontDto data) {
        FavoriteDto favorite = FavoriteDto.builder()
                .boardId(data.getBoardId())
                .memberId(signServiceClient.getIdByNickname(data.getMemberNickname()))
                .build();
        return favoriteServiceClient.toggleFavorite(favorite);
    }

    @PostMapping("/exists")
    public boolean existsFavorite(@RequestBody FavoriteFrontDto data) {
        Long memberId = null;
        if(data.getMemberNickname() != null){
            memberId = signServiceClient.getIdByNickname(data.getMemberNickname());
        }
        FavoriteDto favorite = FavoriteDto.builder()
                .boardId(data.getBoardId())
                .memberId(memberId)
                .build();
        return favoriteServiceClient.existsFavorite(favorite);
    }
}