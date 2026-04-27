package com.traveler.post.domain.post.repository;

import com.traveler.post.domain.post.entity.Post;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT DISTINCT p FROM Post p " + "LEFT JOIN FETCH p.travelPlace tp "
            + "LEFT JOIN FETCH p.images im "
            + "WHERE p.id = :postId ")
    Optional<Post> findByIdWithDetails(Long postId);

    @Query("SELECT pi.imageKey FROM PostImage pi WHERE pi.post.id IN :postIds")
    List<String> findImageKeysByPostIds(@Param("postIds") List<Long> postIds);

    @Query(
            value = "SELECT p.id FROM Post p WHERE p.deleted_at <= :threshold AND p.is_deleted = true",
            nativeQuery = true)
    Slice<Long> findExpiredPostIds(@Param("threshold") LocalDateTime threshold, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Post p WHERE p.id IN :postIds")
    void hardDeletePostsByIds(@Param("postIds") List<Long> postIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    @Query("select p from Post p where p.id = :id")
    Optional<Post> findByIdWithLock(@Param("id") Long id);
}
