package com.traveler.post.domain.like.repository;

import com.traveler.post.domain.like.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    Optional<Like> findByPostIdAndMemberId(Long postId, Long memberId);
}
