package com.traveler.board.service;

import com.traveler.board.entity.Region;
import com.traveler.board.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    private List<String> rList = new ArrayList<>(List.of("서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기"));

    public void initRegion(){
        for(String name : rList){
            Region region = new Region();
            region.setName(name);
            regionRepository.save(region);
        }
    }

    public boolean isEmpty(){
        return regionRepository.count() == 0;
    }

    public Region getRegion(String name){
        return regionRepository.findByName(name).orElseThrow(RuntimeException::new);
    }
}
