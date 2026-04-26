package com.traveler.search.domain.like.service;

import com.traveler.common.api.converter.PageConverter;
import com.traveler.common.core.response.PageResponse;
import com.traveler.search.domain.like.document.LikeDocument;
import com.traveler.search.domain.like.dto.response.LikeSearchResponse;
import com.traveler.search.domain.like.mapper.LikeDocumentMapper;
import com.traveler.search.domain.like.repository.LikeDocumentRepository;
import com.traveler.search.domain.post.document.PostDocument;
import com.traveler.search.domain.post.repository.PostDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class LikeSearchQueryService {
    private final LikeDocumentRepository likeDocumentRepository;
    private final PostDocumentRepository postDocumentRepository;
    private final LikeDocumentMapper likeDocumentMapper;

    public PageResponse<LikeSearchResponse.MyDTO> getMyLikePosts(Long memberId, Pageable pageable) {
        Page<LikeDocument> likes = likeDocumentRepository.findByMemberId(memberId, pageable);
        if (likes.isEmpty()) {
            return PageConverter.emptyPageResponse(pageable.getPageSize());
        }
        List<Long> postIds =
                likes.getContent().stream().map(LikeDocument::getPostId).toList();

        List<PostDocument> posts = postDocumentRepository.findAllByIdIn(postIds);
        Map<Long, PostDocument> postMap =
                posts.stream().collect(Collectors.toMap(PostDocument::getId, Function.identity()));

        List<LikeSearchResponse.MyDTO> content = likes.stream()
                .flatMap(like -> Stream.ofNullable(postMap.get(like.getPostId())))
                .map(likeDocumentMapper::toMyDTO)
                .toList();

        return PageConverter.toPageResponse(new PageImpl<>(content, pageable, likes.getTotalElements()));
    }
}
