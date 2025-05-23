package com.traveler.board.service;

import com.traveler.board.entity.TravelPlace;
import com.traveler.board.repository.TravelPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelPlaceService {
    private final TravelPlaceRepository travelPlaceRepository;
    private final CategoryService categoryService;
    private final RegionService regionService;

    public TravelPlace addAndGetTravelPlace(String category, String region, String name, String address){
        if(categoryService.isEmpty()) categoryService.initCategory();
        if(regionService.isEmpty()) regionService.initRegion();

        TravelPlace travelPlace = new TravelPlace();
        travelPlace.setCategory(categoryService.getCategory(category));
        travelPlace.setRegion(regionService.getRegion(region));
        travelPlace.setName(name);
        travelPlace.setAddress(address);
        return travelPlaceRepository.save(travelPlace);
    }

    public TravelPlace editAndGetTravelPlace(Long id, String category, String region, String name, String address) {
        if (categoryService.isEmpty()) categoryService.initCategory();
        if (regionService.isEmpty()) regionService.initRegion();

        TravelPlace travelPlace = travelPlaceRepository.findById(id).orElseThrow(RuntimeException::new);
        travelPlace.setCategory(categoryService.getCategory(category));
        travelPlace.setRegion(regionService.getRegion(region));
        travelPlace.setName(name);
        travelPlace.setAddress(address);
        return travelPlaceRepository.save(travelPlace);
    }

}
