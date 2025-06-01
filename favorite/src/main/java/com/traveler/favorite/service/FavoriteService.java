package com.traveler.favorite.service;

import com.traveler.favorite.dto.FavoriteDto;
import com.traveler.favorite.entity.Favorite;
import com.traveler.favorite.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;


    @Transactional
    public boolean toggleFavorite(FavoriteDto data){
        Long boardId = data.getBoardId();
        Long memberId = data.getMemberId();

        Optional<Favorite> favorite = favoriteRepository.findByBoardIdAndMemberId(boardId, memberId);
        if (favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
            return false;
        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setBoardId(boardId);
            newFavorite.setMemberId(memberId);
            favoriteRepository.save(newFavorite);
            return true;
        }
    }
    @Transactional
    public boolean existsFavorite(FavoriteDto data){
        Long boardId = data.getBoardId();
        Long memberId = data.getMemberId();
        return favoriteRepository.existsByBoardIdAndMemberId(boardId, memberId);
    }

//    public List<Favorite> listFavorite(String nickname){
//
//    }


//    @Transactional
//    public void updateNickname(String prevNickname, String curNickname){
//        List<Favorite> favoriteList = favoriteRepository.findAllByMemberNickname(prevNickname);
//        for(Favorite favorite : favoriteList){
//            favorite.setMemberNickname(curNickname);
//        }
//    }

}
