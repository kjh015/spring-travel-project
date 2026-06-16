package com.traveler.web.domain.search.facade;

import com.traveler.common.core.response.PageResponse;
import com.traveler.web.domain.member.adaptor.MemberClientAdaptor;
import com.traveler.web.domain.search.client.PostSearchClient;
import com.traveler.web.domain.search.client.dto.response.PostSearchClientResponse;
import com.traveler.web.domain.search.dto.request.PostSearchRequest;
import com.traveler.web.domain.search.dto.response.PostSearchResponse;
import com.traveler.web.domain.search.mapper.PostSearchMapper;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostSearchFacade {
    private final PostSearchClient postSearchClient;
    private final PostSearchMapper postSearchMapper;
    private final MemberClientAdaptor memberClientAdaptor;

    private static final String DEFAULT_NICKNAME = "알 수 없음";

    public PageResponse<PostSearchResponse.ListDTO> search(PostSearchRequest.SearchDTO dto) {
        PageResponse<PostSearchClientResponse.SearchDTO> result = postSearchClient
                .search(postSearchMapper.toSearchClientRequest(dto))
                .result();

        Set<Long> memberIds = result.content().stream()
                .map(PostSearchClientResponse.SearchDTO::memberId)
                .collect(Collectors.toSet());

        Map<Long, String> nicknameMap = memberClientAdaptor.getNicknameMap(memberIds);

        // 게시글 데이터와 닉네임 Map 조합
        return result.map(clientDto -> {
            // 회원이 탈퇴했거나 회원 서비스 장애 시 기본값 제공
            String nickname = nicknameMap.getOrDefault(clientDto.memberId(), DEFAULT_NICKNAME);
            return postSearchMapper.toSearchListResponse(clientDto, nickname);
        });
    }

    public PostSearchResponse.DetailDTO getPostDetail(Long postId) {
        PostSearchClientResponse.DetailDTO result =
                postSearchClient.getPost(postId).result();

        String nickname = memberClientAdaptor.getMemberNickname(result.memberId());

        return postSearchMapper.toDetailResponse(result, nickname);
    }

    public PostSearchResponse.AutocompleteDTO autocomplete(String keyword) {
        return postSearchMapper.toAutocompleteResponse(
                postSearchClient.autocomplete(keyword).result());
    }

    public PageResponse<PostSearchResponse.ListDTO> getMyPosts(Pageable pageable) {
        PageResponse<PostSearchClientResponse.MyDTO> result =
                postSearchClient.getMyPosts(pageable).result();

        String nickname = memberClientAdaptor.getMyNickname();

        return result.map(clientDto -> postSearchMapper.toMyListResponse(clientDto, nickname));
    }
}
