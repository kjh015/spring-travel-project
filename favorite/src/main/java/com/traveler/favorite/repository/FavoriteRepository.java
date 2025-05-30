package com.traveler.favorite.repository;

import com.traveler.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByBoardIdAndMemberNickname(Long boardId, String nickname);
    List<Favorite> findAllByMemberNickname(String nickname);
    boolean existsByBoardIdAndMemberNickname(Long boardId, String nickname);
}
