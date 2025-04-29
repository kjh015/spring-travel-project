package com.traveler.board.repository;


import com.traveler.board.entity.TravelPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPlaceRepository extends JpaRepository<TravelPlace, Long> {
}
