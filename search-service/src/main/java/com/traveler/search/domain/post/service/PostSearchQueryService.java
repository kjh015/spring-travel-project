package com.traveler.search.domain.post.service;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.search.domain.post.converter.PostSearchConverter;
import com.traveler.search.domain.post.document.PostDocument;
import com.traveler.search.domain.post.dto.message.PostSearchMessage;
import com.traveler.search.domain.post.dto.request.PostSearchRequest;
import com.traveler.search.domain.post.dto.response.PostSearchResponse;
import com.traveler.search.domain.post.mapper.PostDocumentMapper;
import com.traveler.search.domain.post.repository.PostDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSearchService {
    private final PostDocumentRepository postDocumentRepository;
    private final PostDocumentMapper postDocumentMapper;

    public PageResponse<PostSearchResponse.SearchDTO> search(PostSearchRequest.SearchDTO dto) {
        Pageable pageable = PostSearchConverter.toPageable(dto);

        Page<PostDocument> result = postDocumentRepository.search(dto, pageable);

        return PageConverter.toPageResponse(result, postDocumentMapper::toSearchDTO);
    }

    public PostSearchResponse.AutocompleteDTO autocomplete(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return new PostSearchResponse.AutocompleteDTO(List.of());
        }

        // 1. Repository에서 데이터 조회 (String 리스트)
        List<String> titles = postDocumentRepository.findAutocompleteTitles(keyword);

        // 2. Mapper를 통해 최종 DTO 변환 및 반환
        return postDocumentMapper.toAutocompleteDTO(titles);
    }

//    public void updatePopularity(PostDocument doc) {
//        double score = (doc.getViewCount() * 0.1) + (doc.getLikeCount() * 0.5) + (doc.getCommentCount() * 0.4);
//
//        // rank_feature는 0보다 큰 양수여야 함
//        doc.setPopularityScore(Math.max(score, 0.0001));
//    }

    public void create(PostSearchMessage.CreatedDTO msg) {}
}
