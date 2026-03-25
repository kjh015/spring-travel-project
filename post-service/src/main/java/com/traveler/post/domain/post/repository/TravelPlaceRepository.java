package com.traveler.post.domain.post.repository;

import com.traveler.post.domain.post.entity.TravelPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPlaceRepository extends JpaRepository<TravelPlace,Long> {
}
