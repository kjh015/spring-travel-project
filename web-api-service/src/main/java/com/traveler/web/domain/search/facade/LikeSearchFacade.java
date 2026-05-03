package com.traveler.web.domain.search.facade;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.search.client.LikeSearchClient;
import com.traveler.web.domain.search.client.dto.response.LikeSearchClientResponse;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import com.traveler.web.domain.search.mapper.LikeSearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeSearchFacade {
    private final LikeSearchClient likeSearchClient;
    private final LikeSearchMapper likeSearchMapper;

    public PageResponse<PostSearchResponse.ListDTO> getMyLikePosts(Pageable pageable) {
        ApiResponse<PageResponse<LikeSearchClientResponse.MyDTO>> response = likeSearchClient.getMyLikePosts(pageable);

        String tmpMemberNickname = "임시 닉네임";

        return response.getResult().map(clientDto -> likeSearchMapper.toMyListResponse(clientDto, tmpMemberNickname));
    }
}
