package com.traveler.post.domain.favorite.controller;

import com.traveler.common.core.code.SuccessCode;
import com.traveler.common.core.response.ApiResponse;
import com.traveler.post.domain.favorite.dto.req.FavoriteReqDTO;
import com.traveler.post.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ApiResponse<Void> addFavorite(@RequestBody FavoriteReqDTO.AddDTO dto) {
        favoriteService.addFavorite(dto);
        return ApiResponse.onSuccess(SuccessCode.CREATED, null);
    }

    @DeleteMapping
    public ApiResponse<Void> removeFavorite(@RequestParam Long postId, @RequestParam Long memberId) {
        favoriteService.removeFavorite(postId, memberId);
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }
}
