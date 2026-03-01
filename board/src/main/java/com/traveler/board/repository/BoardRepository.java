package com.traveler.board.repository;

import com.traveler.board.entity.Board;
import com.traveler.board.repository.querydsl.BoardRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    List<Board> findAll();

    @Query("SELECT b FROM Board b " + "JOIN FETCH b.travelPlace tp "
            + "JOIN FETCH tp.category "
            + "JOIN FETCH tp.region")
    List<Board> findAllWithFetchJoin();

    List<Board> findByIdIn(List<Long> boardIDs);

    List<Board> findAllByMemberId(Long memberId);

    @Modifying
    @Query("UPDATE Board b SET b.viewCount = COALESCE(b.viewCount, 0) + 1 WHERE b.id = :id")
    void increaseViewCount(Long id);
}
