package com.traveler.favorite.repository;

import com.traveler.favorite.entity.Favorite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByBoardIdAndMemberId(Long boardId, Long memberId);

    List<Favorite> findAllByMemberId(Long memberId);

    boolean existsByBoardIdAndMemberId(Long boardId, Long memberId);

    void deleteAllByBoardId(Long boardId);
}
