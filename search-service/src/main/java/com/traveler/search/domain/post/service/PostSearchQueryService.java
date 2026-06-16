package com.traveler.search.domain.post.service;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.search.domain.post.converter.PostSearchConverter;
import com.traveler.search.domain.post.document.PostDocument;
import com.traveler.search.domain.post.dto.request.PostSearchRequest;
import com.traveler.search.domain.post.dto.response.PostSearchResponse;
import com.traveler.search.domain.post.mapper.PostDocumentMapper;
import com.traveler.search.domain.post.repository.PostDocumentRepository;
import com.traveler.search.global.exception.SearchServiceException;
import com.traveler.search.global.exception.code.SearchServiceErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PostSearchQueryService {
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
        List<String> titles = postDocumentRepository.findAutocompleteTitles(keyword);

        return postDocumentMapper.toAutocompleteDTO(titles);
    }

    public PostSearchResponse.DetailDTO getPostDetail(Long postId) {
        PostDocument post = postDocumentRepository
                .findById(postId)
                .orElseThrow(() -> new SearchServiceException(SearchServiceErrorCode.POST_NOT_FOUND));

        return postDocumentMapper.toDetailDTO(post);
    }

    public PageResponse<PostSearchResponse.MyDTO> getMyPosts(Long memberId, Pageable pageable) {
        Page<PostDocument> posts = postDocumentRepository.findByMemberId(memberId, pageable);

        return PageConverter.toPageResponse(posts, postDocumentMapper::toMyDTO);
    }
}
