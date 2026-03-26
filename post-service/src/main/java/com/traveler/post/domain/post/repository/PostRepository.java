package com.traveler.post.domain.post.repository;

import com.traveler.post.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT DISTINCT p FROM Post p " + "LEFT JOIN FETCH p.travelPlace tp "
            + "LEFT JOIN FETCH p.images im "
            + "WHERE p.id = :postId ")
    Optional<Post> findByIdWithDetails(Long postId);

    @Query("SELECT pi.imageKey FROM PostImage pi WHERE pi.post.id IN :postIds")
    List<String> findImageKeysByPostIds(@Param("postIds") List<Long> postIds);

    @Query("SELECT p.id FROM Post p WHERE p.deletedAt <= :threshold AND p.isDeleted = true")
    Slice<Long> findExpiredPostIds(@Param("threshold") LocalDateTime threshold, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Post p WHERE p.id IN :postIds")
    void hardDeletePostsByIds(@Param("postIds") List<Long> postIds);
}
