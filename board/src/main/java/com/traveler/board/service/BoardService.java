package com.traveler.board.service;


import com.traveler.board.dto.BoardDto;
import com.traveler.board.entity.Board;
import com.traveler.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final TravelPlaceService travelPlaceService;

    public List<Board> listArticles() throws DataAccessException{
        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return boardList;
    }

    public void addArticle(BoardDto data)throws DataAccessException{
        Board board = new Board();
        board.setTitle(data.getTitle());
        board.setContent(data.getContent());
        board.setMemberNickname(data.getMemberNickname());
        board.setTravelPlace(travelPlaceService.addAndGetTravelPlace(data.getCategory(), data.getRegion(), data.getTName(), data.getAddress()));

        boardRepository.save(board);
    }

    public Optional<Board> viewArticle(long no){
        return boardRepository.findById(no);
    }

    public void editArticle(BoardDto data){
        Board board = boardRepository.findById(Long.parseLong(data.getNo())).orElseThrow(RuntimeException::new);
        board.setId(Long.parseLong(data.getNo()));
        board.setTitle(data.getTitle());
        board.setContent(data.getContent());
        board.setMemberNickname(data.getMemberNickname());
        board.setTravelPlace(travelPlaceService.editAndGetTravelPlace(board.getTravelPlace().getId(), data.getCategory(), data.getRegion(), data.getTName(), data.getAddress()));

        boardRepository.save(board);
    }
    public void removeArticle(Long no){
        boardRepository.deleteById(no);
    }
}
