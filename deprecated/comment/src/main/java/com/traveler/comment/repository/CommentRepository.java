package com.traveler.comment.repository;

import com.traveler.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);

    List<Comment> findAllByMemberId(Long memberId);

    void deleteAllByBoardId(Long boardId);
}
