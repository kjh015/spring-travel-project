package com.traveler.board.service;


import com.traveler.board.entity.Board;
import com.traveler.board.repository.BoardRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> listArticles() throws DataAccessException{
        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return boardList;
    }

    public void addArticle(Board board)throws DataAccessException{
        boardRepository.save(board);
    }

    public Board viewArticle(long no){
        Optional<Board> optionalBoard = boardRepository.findById(no);
        Board board = null;
        if(optionalBoard.isPresent()){
            board = optionalBoard.get();
        }
        return board;
    }

    public void editArticle(Board inBoard){
        Optional<Board> optionalBoard = boardRepository.findById(inBoard.getId());
        Board board = null;
        if(optionalBoard.isPresent()){
            board = optionalBoard.get();
            board.setTitle(inBoard.getTitle());
            board.setContent(inBoard.getContent());
            boardRepository.save(board);
        }
    }
    public void removeArticle(Long no){
        boardRepository.deleteById(no);
    }
}
