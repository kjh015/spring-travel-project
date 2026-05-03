package com.traveler.web.domain.search.facade;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.search.client.CommentSearchClient;
import com.traveler.web.domain.search.client.dto.response.CommentSearchClientResponse;
import com.traveler.web.domain.search.dto.response.CommentSearchResponse;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import com.traveler.web.domain.search.mapper.CommentSearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentSearchFacade {
    private final CommentSearchClient commentSearchClient;
    private final CommentSearchMapper commentSearchMapper;

    public PageResponse<CommentSearchResponse.ListDTO> getComments(Long postId, Pageable pageable) {
        ApiResponse<PageResponse<CommentSearchClientResponse.ListDTO>> response =
                commentSearchClient.getComments(postId, pageable);

        String tmpMemberNickname = "임시 닉네임";

        return response.getResult().map(clientDto -> commentSearchMapper.toListResponse(clientDto, tmpMemberNickname));
    }

    public PageResponse<PostSearchResponse.ListDTO> getMyComments(Pageable pageable) {
        ApiResponse<PageResponse<CommentSearchClientResponse.MyDTO>> response =
                commentSearchClient.getMyComments(pageable);

        String tmpMemberNickname = "임시 닉네임";

        return response.getResult()
                .map(clientDto -> commentSearchMapper.toMyListResponse(clientDto, tmpMemberNickname));
    }
}
