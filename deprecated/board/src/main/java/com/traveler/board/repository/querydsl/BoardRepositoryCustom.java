package com.traveler.board.repository.querydsl;

import com.traveler.board.dto.BoardListDto;
import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardListDto> findBoardList();
}
