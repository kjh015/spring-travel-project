package com.traveler.search.domain.post.converter;

import com.traveler.search.domain.post.dto.request.PostSearchRequest;
import com.traveler.search.domain.post.enums.PostSortType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PostSearchConverter {
    public static Pageable toPageable(PostSearchRequest.SearchDTO dto) {
        PostSortType sortType = (dto.sort() == null) ? PostSortType.ACCURACY : dto.sort();
        Sort.Direction direction = (dto.direction() == null) ? Sort.Direction.DESC : dto.direction();
        int page = (dto.page() == null || dto.page() < 0) ? 0 : dto.page();
        int size = (dto.size() == null || dto.size() <= 0) ? 10 : dto.size();

        Sort sort = sortType.getSort(direction).and(PostSortType.LATEST.getSort(direction));

        return PageRequest.of(page, size, sort);
    }
}
