package com.traveler.favorite.service;

import com.traveler.favorite.dto.FavoriteDto;
import com.traveler.favorite.entity.Favorite;
import com.traveler.favorite.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;


    @Transactional
    public void toggleFavorite(FavoriteDto data){
        Long boardId = Long.valueOf(data.getBoardId());
        String nickname = data.getMemberNickname();

        Optional<Favorite> favorite = favoriteRepository.findByBoardIdAndMemberNickname(boardId, nickname);
        if (favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setBoardId(boardId);
            newFavorite.setMemberNickname(nickname);
            favoriteRepository.save(newFavorite);
        }
    }

    @Transactional
    public void updateNickname(String prevNickname, String curNickname){
        List<Favorite> favoriteList = favoriteRepository.findAllByMemberNickname(prevNickname);
        for(Favorite favorite : favoriteList){
            favorite.setMemberNickname(curNickname);
        }
    }
}
