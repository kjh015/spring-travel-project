package com.traveler.board.service;


import com.traveler.board.dto.BoardDto;
import com.traveler.board.dto.BoardListDto;
import com.traveler.board.entity.Board;
import com.traveler.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final TravelPlaceService travelPlaceService;

    public List<BoardListDto> listArticles() throws DataAccessException{
        List<BoardListDto> boardList = boardRepository.findAllBoardListDto();
        return boardList;
    }

    public void addArticle(BoardDto data)throws DataAccessException{
        Board board = new Board();
        board.setTitle(data.getTitle());
        board.setContent(data.getContent());
        board.setMemberId(data.getMemberId());
        System.out.println("tName: " + data.getTravelPlace());
        board.setTravelPlace(travelPlaceService.addAndGetTravelPlace(data.getCategory(), data.getRegion(), data.getTravelPlace(), data.getAddress()));

        boardRepository.save(board);
    }

    public BoardDto viewArticle(long no){
        Board board = boardRepository.findById(no).orElseThrow(RuntimeException::new);
        BoardDto dto = new BoardDto();
        dto.setNo(board.getId().toString());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setMemberId(board.getMemberId());
        dto.setTravelPlace(board.getTravelPlace().getName());
        dto.setAddress(board.getTravelPlace().getAddress());
        dto.setRegion(board.getTravelPlace().getRegion().getName());
        dto.setCategory(board.getTravelPlace().getCategory().getName());
        return dto;
    }

    public void editArticle(BoardDto data){
        Board board = boardRepository.findById(Long.parseLong(data.getNo())).orElseThrow(RuntimeException::new);
        board.setId(Long.parseLong(data.getNo()));
        board.setTitle(data.getTitle());
        board.setContent(data.getContent());
        board.setMemberId(data.getMemberId());
        board.setTravelPlace(travelPlaceService.editAndGetTravelPlace(board.getTravelPlace().getId(), data.getCategory(), data.getRegion(), data.getTravelPlace(), data.getAddress()));

        boardRepository.save(board);
    }
    public void removeArticle(Long no){
        boardRepository.deleteById(no);
    }
}
