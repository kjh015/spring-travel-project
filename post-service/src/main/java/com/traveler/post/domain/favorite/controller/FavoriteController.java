package com.traveler.post.domain.favorite.controller;

import com.traveler.common.api.auth.LoginUser;
import com.traveler.common.api.auth.UserContext;
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
    public ApiResponse<Void> addFavorite(@RequestBody FavoriteReqDTO.AddDTO dto, @LoginUser UserContext user) {
        favoriteService.addFavorite(dto, user.id());
        return ApiResponse.onSuccess(SuccessCode.CREATED, null);
    }

    @DeleteMapping
    public ApiResponse<Void> removeFavorite(@RequestParam Long postId, @LoginUser UserContext user) {
        favoriteService.removeFavorite(postId, user.id());
        return ApiResponse.onSuccess(SuccessCode.OK, null);
    }
}
