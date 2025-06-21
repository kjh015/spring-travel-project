package com.traveler.board.repository;


import com.traveler.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAll();
    List<Board> findByIdIn(List<Long> boardIDs);
    List<Board> findAllByMemberId(Long memberId);
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = COALESCE(b.viewCount, 0) + 1 WHERE b.id = :id")
    void increaseViewCount(Long id);




}
