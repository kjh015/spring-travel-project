package com.traveler.post.domain.favorite.repository;

import com.traveler.post.domain.favorite.entity.Favorite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    Optional<Favorite> findByPostIdAndMemberId(Long postId, Long memberId);
}
