package com.traveler.web.domain.search.facade;

import com.traveler.common.core.response.ApiResponse;
import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.search.client.PostSearchClient;
import com.traveler.web.domain.search.client.dto.response.PostSearchClientResponse;
import com.traveler.web.domain.search.dto.request.PostSearchRequest;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import com.traveler.web.domain.search.mapper.PostSearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostSearchFacade {
    private final PostSearchClient postSearchClient;
    private final PostSearchMapper postSearchMapper;

    public PageResponse<PostSearchResponse.ListDTO> search(PostSearchRequest.SearchDTO dto) {
        ApiResponse<PageResponse<PostSearchClientResponse.SearchDTO>> response =
                postSearchClient.search(postSearchMapper.toSearchClientRequest(dto));

        String tmpMemberNickname = "임시 닉네임";

        return response.result().map(clientDto -> postSearchMapper.toSearchListResponse(clientDto, tmpMemberNickname));
    }

    public PostSearchResponse.DetailDTO getPostDetail(Long postId) {
        ApiResponse<PostSearchClientResponse.DetailDTO> response = postSearchClient.getPost(postId);

        String tmpMemberNickname = "임시 닉네임";

        return postSearchMapper.toDetailResponse(response.result(), tmpMemberNickname);
    }

    public PostSearchResponse.AutocompleteDTO autocomplete(String keyword) {
        ApiResponse<PostSearchClientResponse.AutocompleteDTO> response = postSearchClient.autocomplete(keyword);

        return postSearchMapper.toAutocompleteResponse(response.result());
    }

    public PageResponse<PostSearchResponse.ListDTO> getMyPosts(Pageable pageable) {
        ApiResponse<PageResponse<PostSearchClientResponse.MyDTO>> response = postSearchClient.getMyPosts(pageable);

        String tmpMemberNickname = "임시 닉네임";

        return response.result().map(clientDto -> postSearchMapper.toMyListResponse(clientDto, tmpMemberNickname));
    }
}
