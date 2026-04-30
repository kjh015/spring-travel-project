package com.traveler.board.repository;

import com.traveler.board.entity.Board;
import com.traveler.board.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByBoard(Board board);
}
