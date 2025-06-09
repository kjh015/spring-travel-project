package com.traveler.board.repository;


import com.traveler.board.dto.BoardListDto;
import com.traveler.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT new com.traveler.board.dto.BoardListDto(b.id, b.title, b.memberId, b.modifiedDate) FROM Board b")
    List<BoardListDto> findAllBoardListDto();
    List<Board> findByIdIn(List<Long> boardIDs);
    List<Board> findAllByMemberId(Long memberId);

}
