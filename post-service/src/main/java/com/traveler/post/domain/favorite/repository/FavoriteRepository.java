package com.traveler.post.domain.favorite.repository;

import com.traveler.post.domain.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    Optional<Favorite> findByPostIdAndMemberId(Long postId, Long memberId);
}
