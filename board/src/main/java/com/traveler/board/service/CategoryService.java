package com.traveler.board.service;

import com.traveler.board.entity.Category;
import com.traveler.board.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private List<String> cList = new ArrayList<>(List.of("축제", "공연", "행사", "체험", "쇼핑", "자연", "역사", "가족", "음식"));

    public void initCategory(){
        for(String name : cList){
            Category category = new Category();
            category.setName(name);
            categoryRepository.save(category);
        }
    }

    public boolean isEmpty(){
        return categoryRepository.count() == 0;
    }

    public Category getCategory(String name){
        return categoryRepository.findByName(name).orElseThrow(RuntimeException::new);
    }


    

}
