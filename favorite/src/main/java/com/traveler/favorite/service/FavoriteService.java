package com.traveler.favorite.service;

import com.traveler.favorite.dto.FavoriteDto;
import com.traveler.favorite.entity.Favorite;
import com.traveler.favorite.kafka.KafkaService;
import com.traveler.favorite.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final KafkaService kafkaService;


    @Transactional
    public boolean toggleFavorite(FavoriteDto data){
        Long boardId = data.getBoardId();
        Long memberId = data.getMemberId();
        if(memberId == null) return false;

        Optional<Favorite> favorite = favoriteRepository.findByBoardIdAndMemberId(boardId, memberId);
        if (favorite.isPresent()) {
            kafkaService.updateFavoriteCount(favorite.get().getBoardId(), false);
            favoriteRepository.delete(favorite.get());
            return false;
        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setBoardId(boardId);
            newFavorite.setMemberId(memberId);
            Favorite savedFavorite = favoriteRepository.save(newFavorite);
            kafkaService.updateFavoriteCount(savedFavorite.getBoardId(), true);
            return true;
        }
    }
    @Transactional
    public boolean existsFavorite(FavoriteDto data){
        Long boardId = data.getBoardId();
        Long memberId = data.getMemberId();
        if(memberId == null) return false;
        return favoriteRepository.existsByBoardIdAndMemberId(boardId, memberId);
    }

    public Set<Long> listFavorite(Long memberId){
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return favorites.stream().map(Favorite::getBoardId).collect(Collectors.toSet());

    }

}
