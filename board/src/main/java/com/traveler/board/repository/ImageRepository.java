package com.traveler.board.repository;


import com.traveler.board.entity.Board;
import com.traveler.board.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByBoard(Board board);
}
