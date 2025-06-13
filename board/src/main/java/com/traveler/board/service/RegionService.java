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

    private List<String> rList = new ArrayList<>(List.of("강원", "경기", "대구", "부산", "서울", "인천", "전남", "제주", "기타"));

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
