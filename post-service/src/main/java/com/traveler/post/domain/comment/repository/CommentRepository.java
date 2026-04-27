package com.traveler.post.domain.comment.repository;

import com.traveler.post.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(
            value = "SELECT c.id FROM comment c WHERE c.deleted_at <= :threshold AND c.is_deleted = true",
            nativeQuery = true)
    Slice<Long> findExpiredCommentIds(@Param("threshold") LocalDateTime threshold, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Comment c WHERE c.id IN :commentIds")
    void hardDeleteCommentsByIds(@Param("commentIds") List<Long> commentIds);
}
