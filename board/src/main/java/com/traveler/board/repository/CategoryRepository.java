package com.traveler.board.repository;


import com.traveler.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Board, Long> {
}
